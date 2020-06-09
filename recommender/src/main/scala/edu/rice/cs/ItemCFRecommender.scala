package edu.rice.cs

import com.mongodb.casbah.{MongoClient, MongoClientURI}
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object ItemCFRecommender {
  val MONGODB_REVIEW_COLLECTION = "review"
  val MONGODB_ITEMCF_REC_COLLECTION = "itemcf_recommendation"
  val RECOMMENDATION_NUM = 20

  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb+srv://amazon:amazon666@cluster0-u2qt7.mongodb.net/amazon_recommender?retryWrites=true&w=majority",
      "mongo.db" -> "amazon_recommender"
    )

    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("TrendingRecommender")
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._
    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))

    val ratingsDF = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_REVIEW_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Review]
      .map(
        review => (review.userId, review.productId, review.rate)
      )
      .toDF("userId", "productId", "rate")

    val ratingCountDF = ratingsDF.groupBy("productId").count()

    /*
      +---------+------+-----+-----+
      |productId|userId|score|count|
      +---------+------+-----+-----+
      |505556   |13784 |3.0  |172  |
     */
    val ratingWithCountDF = ratingsDF.join(ratingCountDF, "productId")

    // create product-product pair
    val crossProductDF = ratingWithCountDF.join(ratingWithCountDF, "userId")
      .toDF("userId", "product1", "score1", "count1", "product2", "score2", "count2")
      .select("userId", "product1", "count1", "product2", "count2")
    crossProductDF.createOrReplaceTempView("cross_product")

    // (productId, [(productId1, score), ...])
    val crossProductCountDF = spark.sql(
      "select product1, product2, count(userId) as conCount, first(count1) as count1, first(count2) as count2 " +
        "from cross_product " +
        "group by product1, product2"
    ).cache()

    val itemCF = crossProductCountDF.map {
      item =>
        val sim = getSimilarity(
          item.getAs[Long]("conCount"),
          item.getAs[Long]("count1"),
          item.getAs[Long]("count2")
        )
        (item.getString(0), RecommendItem(item.getString(1), sim))
    }
      .rdd
      .groupByKey()
      .map {
        case (productId, recommendations) => {
          ProductRecList(
            productId,
            recommendations.toList
              .filter(x => x.productId != productId) // filter product self-self pairs
              .sortWith(_.score > _.score)
              .take(RECOMMENDATION_NUM)
          )
        }
      }
      .toDF()

    saveToMongoDB(itemCF, MONGODB_ITEMCF_REC_COLLECTION, "productId");
  }

  def getSimilarity(conCount: Long, count1: Long, count2: Long): Double = {
    conCount / math.sqrt(count1 * count2)
  }

  def saveToMongoDB(df: DataFrame, collectionName: String, index: String)(implicit mongoConfig: MongoConfig): Unit = {
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))

    val mongoCollection = mongoClient(mongoConfig.db)(collectionName)
    mongoCollection.dropCollection()

    df.show(10)
    df.write
      .option("uri", mongoConfig.uri)
      .option("collection", collectionName)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    mongoCollection.createIndex(MongoDBObject(index -> 1))
    mongoClient.close()
  }
}
