package edu.rice.cs

import java.text.SimpleDateFormat
import java.util.Date

import com.mongodb.casbah.{MongoClient, MongoClientURI}
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object TrendingRecommender {
  val MONGODB_REVIEW_COLLECTION = "review"
  val MONGODB_TRENDING_REC_COLLECTION = "trending_recommendation"
  val MONGODB_TOPRATE_REC_COLLECTION = "top_rate_recommendation"

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

    val ratingDF = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_REVIEW_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Review]
      .toDF()
    // create a temporary table
    ratingDF.createOrReplaceTempView("ratings")

    // Get trending products in the last month
    val simpleDateFormat = new SimpleDateFormat("yyyyMM")
    spark.udf.register("toMonth", (x: Int) => simpleDateFormat.format(new Date(x * 1000L)).toInt)
    val ratingsToMonthDF = spark.sql("select productId, rate, toMonth(timestamp) as month from ratings")
    ratingsToMonthDF.createOrReplaceTempView("ratingsToMonth")
    val ratingsLastMonthDF = spark.sql(
      "select productId, count(productId) as count, month " +
      "from ratingsToMonth group by month, productId " +
      "order by month desc, count desc limit 20"
    )
    saveToMongoDB(ratingsLastMonthDF, MONGODB_TRENDING_REC_COLLECTION, "productId")

    // Get top-rating products in the history
    val topRatingProductDF = spark.sql(
      "select productId, avg(rate) as score from ratings " +
        "group by productId " +
        "order by score desc limit 20")
    saveToMongoDB(topRatingProductDF, MONGODB_TOPRATE_REC_COLLECTION, "productId")

    spark.stop()
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
