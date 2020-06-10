package edu.rice.cs

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.lit


object DataLoader {
  // local db
//  val config = Map(
//    "spark.cores" -> "local[*]",
//    "mongo.uri" -> "mongodb://127.0.0.1:27017/recommender",
//    "mongo.db" -> "recommender"
//  )

  // test db
  val PRODUCT_PATH = "./recommender/src/resources/meta_Movies_and_TV_test.json"
  val RATING_PATH = "./recommender/src/resources/ratings_Movies_and_TV_test.csv"
  val REVIEW_PATH = "./recommender/src/resources/reviews_Movies_and_TV_5_test.json"
  val config = Map(
    "spark.cores" -> "local[*]",
    "mongo.uri" -> "mongodb+srv://amazon:amazon666@cluster0-u2qt7.mongodb.net/test?retryWrites=true&w=majority",
    "mongo.db" -> "test"
  )

  // online db
//  val PRODUCT_PATH = "./recommender/src/resources/meta_Movies_and_TV.json"
//  val RATING_PATH = "./recommender/src/resources/ratings_Movies_and_TV.csv"
////  val REVIEW_PATH = "./recommender/src/resources/reviews_Movies_and_TV_5.json"
//  val config = Map(
//    "spark.cores" -> "local[*]",
//    "mongo.uri" -> "mongodb+srv://amazon:amazon666@cluster0-u2qt7.mongodb.net/amazon_recommender?retryWrites=true&w=majority",
//    "mongo.db" -> "amazon_recommender"
//  )

  // collection name
  val PRODUCT_COLLECTION = "product"
//  val REVIEW_COLLECTION = "review"
  val RATING_COLLECTION = "rating"
  val USER_COLLECTION = "user"


  def main(args: Array[String]): Unit = {
    // create a spark config
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("DataLoader")
    // create a spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    import spark.implicits._
    // load product
    val productDF = spark.read.json(PRODUCT_PATH)
      .withColumnRenamed("asin", "productId")
      .select("productId", "categories", "description", "imUrl", "price", "title")
      .na.drop(Seq("productId", "title"))
    val ratingDF = spark.read.format("csv")
      .option("inferSchema", "true")
      .load(RATING_PATH)
      .toDF("userId", "productId", "rating", "timestamp")

    val userDF = ratingDF
      .select("userId")
      .withColumn("password", lit("default"))
      .dropDuplicates()
//    productDF.printSchema()
//    ratingDF.printSchema()
//    userDF.printSchema()

    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))
    saveToMongoDB(productDF, PRODUCT_COLLECTION, "productId")
    saveToMongoDB(ratingDF, RATING_COLLECTION, "userId")
    saveToMongoDB(userDF, USER_COLLECTION, "userId")

    spark.stop()
  }

  def saveToMongoDB(df: DataFrame, collectionName: String, index: String)(implicit mongoConfig: MongoConfig): Unit = {
    // create a mongoDB client
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))
    // create a mongoDB collection
    val mongoCollection = mongoClient(mongoConfig.db)(collectionName)
    // drop the collection if exist
    mongoCollection.dropCollection()

    df.show(10)
    df.write
      .option("uri", mongoConfig.uri)
      .option("collection", collectionName)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    // create index
    mongoCollection.createIndex(MongoDBObject(index -> 1))
    mongoClient.close()
  }
}
