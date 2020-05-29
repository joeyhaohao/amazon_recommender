package edu.rice.cs

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.kafka.common.serialization.StringDeserializer
import redis.clients.jedis.Jedis

object RealtimeRecommender {
  val MONGODB_REVIEW_COLLECTION = "review"
  val REALTIME_RECOMMENDATION_COLLECTION = "realtime_recommendation"
  val PRODUCT_RECOMMENDATION_COLLECTION = "product_recommendation"

  val USER_RATING_NUM = 20
  val SIMILAR_PRODUCTS_NUM = 20

  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb+srv://amazon:amazon666@cluster0-u2qt7.mongodb.net/amazon_recommender?retryWrites=true&w=majority",
      "mongo.db" -> "amazon_recommender",
      "kafka.topic" -> "rating"
    )

    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("RealtimeRecommender")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    // duration long enough to finish real-time computation
    val ssc = new StreamingContext(sc, Seconds(2))

    import spark.implicits._
    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))
    val mongoClient = MongoClient("mongodb+srv://amazon:amazon666@cluster0-u2qt7.mongodb.net/amazon_recommender?retryWrites=true&w=majority")
    lazy val jedis = new Jedis("127.0.0.1")

    // product similarity matrix
    val productSimMatrix = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", PRODUCT_RECOMMENDATION_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[RecommendResult]
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

    // userId|productId|score|timestamp
    val ratingStream = kafkaStream.map { msg =>
      var attr = msg.value().split("\\|")
      (attr(0), attr(1), attr(2).toDouble, attr(3).toInt)
    }

    ratingStream.foreachRDD {
      rdds =>
        rdds.foreach {
          case (userId, productId, score, timestamp) =>
            println("new rating: " + userId + "|" + productId + "|" + score + "|" + timestamp)

            // retrieve the latest ratings of user
            val userLatestRatings = getUserRecentRatings(userId, USER_RATING_NUM, jedis)
//            userLatestRatings.foreach(println)

            // 2. 从相似度矩阵中获取当前商品最相似的商品列表，作为备选列表，保存成一个数组 Array[productId]
//            val similarProducts = getSimilarProducts(userId, productId, SIMILAR_PRODUCTS_NUM, productSimMatrixBcast.value, mongoClient)

//            // 3. 计算每个备选商品的推荐优先级，得到当前用户的实时推荐列表，保存成 Array[(productId, score)]
//            val streamRecs = computeProductScore(candidateProducts, userRecentlyRatings, simProductsMatrixBC.value)
//
//            // 4. 把推荐列表保存到 mongodb
//            saveDataToMongoDB(userId, streamRecs)
        }
    }

    ssc.start()
    println("Spark streaming started")
    ssc.awaitTermination()
  }

  /**
   * retrieve the latest #num ratings data of user from Redis
   * Redis data format: KEY(userId: id) ----- VALUE(productId:score)
   * @return Array[(productId, score)]
   */

  import scala.collection.JavaConversions._

  def getUserRecentRatings(userId: String, num: Int, jedis: Jedis): Array[(String, Double)] = {
    println(userId)
    jedis.lrange("userId:" + userId.toString, 0, num)
      .map { item =>
        println(item)
        val attr = item.split("\\:")
        (attr(0).trim, attr(1).trim.toDouble)
      }
      .toArray
  }

  /**
   * 从相似度矩阵中获取当前商品最相似的商品列表，作为备选列表，保存成一个数组 Array[productId]
   * 获取当前商品的相似列表，并过滤掉用户已经评分过的，作为备选列表
   */
  /**
   *
   * @param num         取前 num 个数据
   * @param productId   需要寻找与之相似商品的 productId
   * @param userId      商品推荐人
   * @param productSimMatrix 相似度矩阵，从 ProductRecs 里面获取的，与该 productId 相似的 ( productId, score )
   * @param mongoConfig MongoDB 连接配置
   * @return
   */
  def getSimilarProducts(userId: String,
                         productId: String,
                         num: Int,
                         productSimMatrix: scala.collection.Map[String, scala.collection.immutable.Map[String, Double]],
                         mongoClient: MongoClient) // Map 里面套 Map
                        (implicit mongoConfig: MongoConfig): Array[String] = {
    // 从广播变量相似度矩阵中（simProducts，他是一个 map，KEY 是 productId，VALUE 是 score）拿到当前商品的相似度列表
    val similarProducts = productSimMatrix(productId).toArray // 取出对应 productId 的评分列表

    // 从 Rating 中获得用户已经评分过的商品，过滤掉，排序输出
    val reviewCollection = mongoClient(mongoConfig.db)(MONGODB_REVIEW_COLLECTION)
    val ratingExist = reviewCollection.find(MongoDBObject("userId" -> userId)) // 从已获取的 Rating 中过滤出只含 userId 的数据
      .toArray
      .map {
        item => // 在（userId,productId,score,timestamp）中只需要 productId
          item.get("productId").toString
      }
    // 从所有的相似商品中进行过滤
    // 如果 allSimProducts 里面的某一项在 ratingExist 里面的话就要过滤，应该 productId 取不在 ratingExist 里面的数据
    similarProducts.filter(x => !ratingExist.contains(x._1)) // x 的格式上岗面提到过，就是(productId, score)
      .sortWith(_._2 > _._2) // 按照 score 降序排序
      .take(num)
      .map(x => x._1) // 最后只需要得到 productId 就行了，所以要将二元组转为一元组
  }
}
