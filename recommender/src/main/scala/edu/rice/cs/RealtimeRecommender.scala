package edu.rice.cs

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.kafka.common.serialization.StringDeserializer
import redis.clients.jedis.Jedis
import org.apache.log4j.Logger

object RealtimeRecommender {

  val RATING_COLLECTION = "rating"
  val REALTIME_REC_COLLECTION = "realtime_recommendation"
  val PRODUCT_SIM_COLLECTION = "product_similarity"
  val RECENT_EVENT_NUM = 20
  val SIMILAR_PRODUCTS_NUM = 20
  val logger = Logger.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setMaster(config("spark.cores"))
      .setAppName("RealtimeRecommender")
//      .set("spark.testing.memory", "2147480000")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    // duration long enough to finish real-time computation
    val ssc = new StreamingContext(sc, Seconds(2))

    import spark.implicits._
    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))

    // product similarity matrix
    val productSimMatrix = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", PRODUCT_SIM_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[ProductRecList]
      .rdd
      .map {
        item =>
          (item.productId, item.recommendations.map(x => (x.productId, x.score)).toMap)
      }
      .collectAsMap()
    // broadcast product sim matrix
    val productSimMatrixBcast = sc.broadcast(productSimMatrix)

    val kafkaParam = Map(
      "bootstrap.servers" -> "127.0.0.1:9092",
//      "bootstrap.servers" -> "ec2-18-217-80-11.us-east-2.compute.amazonaws.com:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "rating",
      "auto.offset.reset" -> "latest"
    )

    // create Kafka datastream
    val kafkaStream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(config("kafka.topic")), kafkaParam)
    )

    // event|userId|productId|score|timestamp
    val eventStream = kafkaStream.map { msg =>
      println(msg)
      var attr = msg.value().split("\\|")
      (attr(0), attr(1), attr(2), attr(3).toDouble, attr(4).toInt)
    }

    eventStream.foreachRDD {
      rdds =>
        rdds.foreach {
          case (event, userId, productId, score, timestamp) =>
            println("new event: " + event + "|" + userId + "|" + productId + "|" + score + "|" + timestamp)

            // retrieve the latest ratings of the user
            val latestEvents = getUserRecentEvents(userId, RECENT_EVENT_NUM)
            latestEvents.foreach(println)

            // retrieve similar products from product similarity matrix
            val similarProducts = getSimilarProducts(userId, productId, SIMILAR_PRODUCTS_NUM, productSimMatrixBcast.value)

            // compute product scores and generate real-time recommendation
            val recommendList = generateRecommendList(similarProducts, latestEvents, productSimMatrixBcast.value)
            println("realtime recommendation list update")
            recommendList.foreach(println)

            saveToMongoDB(userId, recommendList)
        }
    }

    ssc.start()
    println("Spark streaming started")
    ssc.awaitTermination()
  }

  /**
   * Retrieve the latest #num ratings data of user from Redis
   * Redis data format: KEY(userId: id) ----- VALUE(productId:score)
   *
   * @return Array[(productId, score)]
   */
  import scala.collection.JavaConversions._

  def getUserRecentEvents(userId: String, num: Int): Array[(String, String, Double)] = {
    val jedis = new Jedis("127.0.0.1")
//    val jedis = new Jedis("ec2-18-217-80-11.us-east-2.compute.amazonaws.com")
    jedis.lrange("userId:" + userId.toString, 0, num)
      .map { item =>
        val attr = item.split("\\|")
        (attr(0).trim(), attr(1).trim, attr(2).trim.toDouble)
      }
      .toArray
  }

  /**
   * Retrieve similar products, filter out those rated by user
   *
   * @return Array[productId]
   */
  def getSimilarProducts(userId: String,
                         productId: String,
                         num: Int,
                         productSimMatrix: scala.collection.Map[String, scala.collection.immutable.Map[String, Double]])
                        (implicit mongoConfig: MongoConfig): Array[String] = {
    // retrieve similarity list of current product
    // Map[productId, score]
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))
    val similarProducts = productSimMatrix(productId).toArray

    // filter rated products
    val ratingCollection = mongoClient(mongoConfig.db)(RATING_COLLECTION)
    val ratedProducts = ratingCollection.find(MongoDBObject("userId" -> userId))
      .toArray
      .map {
        item => item.get("productId").toString
      }

    similarProducts
      .filter(
        x => !ratedProducts.contains(x._1)
      )
      .sortWith(_._2 > _._2) // sort by score
      .take(num)
      .map(x => x._1)
  }

  /**
   * Compute candidate scores of every candidate product and generate real-time recommendation list
   * score(i) = sum(similarity(i,j) * rating(j))
   *
   * @return Array[(productId, score)]
   */
  def generateRecommendList(candidates: Array[String],
                            events: Array[(String, String, Double)],
                            productSimMatrix: scala.collection.Map[String, scala.collection.immutable.Map[String, Double]]): Array[(String, Double)] = {
    // base score of products, (productId, score)
    val scores = scala.collection.mutable.ArrayBuffer[(String, Double)]()

    // high-rating product counter
    val increMap = scala.collection.mutable.HashMap[String, Int]()
    // low-rating product counter
    val decreMap = scala.collection.mutable.HashMap[String, Int]()

    for (product <- candidates; event <- events) {
      // get similarity between candidate product and rated product
      val simScore = getProductsSimScore(product, event._2, productSimMatrix)
      if (simScore > 0.5) {
        // sum(similarity * rating)
        scores += ((product, simScore * event._3))
        if (event._3 >= 4) {
          // high-rating/search product
          increMap(product) = increMap.getOrDefault(product, 0) + 1
        } else {
          decreMap(product) = decreMap.getOrDefault(product, 0) + 1
        }
      }
    }

    // compute priority of all products
    scores.groupBy(_._1).map {
      // scores: list of scores
      case (productId, scores) =>
        (productId, scores.map(_._2).sum / scores.length + log(increMap.getOrDefault(productId, 1)) - log(decreMap.getOrDefault(productId, 1)))
    }
      .toArray
      .sortWith(_._2 > _._2)  // sort by scores
  }


  def getProductsSimScore(product1: String, product2: String,
                          productSimMatrix: scala.collection.Map[String, scala.collection.immutable.Map[String, Double]]): Double = {
    // return 0 if not found in product similarity matrix
    productSimMatrix.get(product1) match {
      case Some(sims) => sims.get(product2) match {
        case Some(score) => score
        case None => 0.0
      }
      case None => 0.0
    }
  }

  // base-10 log
  def log(m: Int): Double = {
    val N = 10
    math.log(m) / math.log(N)
  }

  def saveToMongoDB(userId: String, recommendList: Array[(String, Double)])(implicit mongoConfig: MongoConfig): Unit = {
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))
    val mongoCollection = mongoClient(mongoConfig.db)(REALTIME_REC_COLLECTION)

    mongoCollection.findAndRemove(MongoDBObject("userId" -> userId))
    mongoCollection.insert(
      MongoDBObject(
        "userId" -> userId,
        "recommendations" -> recommendList.map(
          x => MongoDBObject("productId" -> x._1, "score" -> x._2)
        )
      )
    )
    logger.warn("Save %d recommendations to mongoDB".format(recommendList.size))
  }

}
