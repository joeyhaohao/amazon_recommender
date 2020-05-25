package edu.rice.cs

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * product data
 * productID      0000143561
 * title          Everyday Italian (with Giada de Laurentiis)...
 * categories     [['Movies & TV', 'Movies']]
 * imUrl          http://g-ecx.images-amazon.com/images/G/01/x-site/icons/no-img-sm._CB192198896_.gif
 */
//case class Product(productID: String, title: String, categories: List[String], imUrl: String)

object DataLoader {
  // file path
  val PRODUCT_PATH = "./recommender/src/resources/meta_Movies_and_TV_test.json"
  val REVIEW_PATH = "./recommender/src/resources/reviews_Movies_and_TV_5_test.json"
  // collection name
  val MONGODB_PRODUCT_COLLECTION = "product"
  val MONGODB_REVIEW_COLLECTION = "review"
  val MONGODB_USER_COLLECTION = "user"

  def main(args: Array[String]): Unit = {
//    val config = Map(
//      "spark.cores" -> "local[*]",
//      "mongo.uri" -> "mongodb://127.0.0.1:27017/recommender",
//      "mongo.db" -> "recommender"
//    )
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb+srv://amazon:amazon666@cluster0-u2qt7.mongodb.net/amazon_recommender?retryWrites=true&w=majority",
      "mongo.db" -> "amazon_recommender"
    )
    // create a spark config
    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("DataLoader")
    // create a spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    // load product
    val productDF = spark.read.json(PRODUCT_PATH)
    val reviewDF = spark.read.json(REVIEW_PATH)
    val userDF = reviewDF
      .select("reviewerID", "reviewerName")
      .toDF("userId", "username")
      .dropDuplicates()
    productDF.printSchema()
    reviewDF.printSchema()
    userDF.printSchema()

    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))
    storeDataInMongoDB(productDF, reviewDF, userDF)

    spark.stop()
  }

  def storeDataInMongoDB(productDF: DataFrame, reviewDF: DataFrame, userDF: DataFrame)(implicit mongoConfig: MongoConfig): Unit = {
    // create a mongoDB client
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))
    // create a mongoDB collection
    val productCollection = mongoClient(mongoConfig.db)(MONGODB_PRODUCT_COLLECTION)
    val reviewCollection = mongoClient(mongoConfig.db)(MONGODB_REVIEW_COLLECTION)
    val userCollection = mongoClient(mongoConfig.db)(MONGODB_USER_COLLECTION)
    // drop the collection if exist
    productCollection.dropCollection()
    reviewCollection.dropCollection()
    userCollection.dropCollection()

    // load data into the collection
    productDF.write
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_PRODUCT_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    reviewDF.write
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_REVIEW_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    userDF.write
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_USER_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

    // create index
    productCollection.createIndex(MongoDBObject("asin" -> 1))
    reviewCollection.createIndex(MongoDBObject("reviewerID" -> 1))
    reviewCollection.createIndex(MongoDBObject("asin" -> 1))
    userCollection.createIndex(MongoDBObject("reviewerID" -> 1))

    mongoClient.close()
  }
}
