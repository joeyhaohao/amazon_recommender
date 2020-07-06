package edu.rice.cs

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.lit
import org.apache.log4j.Logger

object DataLoader {

  // collection name
  val PRODUCT_COLLECTION = "product"
//  val REVIEW_COLLECTION = "review"
  val RATING_COLLECTION = "rating"
  val USER_COLLECTION = "user"
  val logger = Logger.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    // create a spark config
    val sparkConf = new SparkConf()
      .setMaster(config("spark.cores"))
      .setAppName("DataLoader")
    // create a spark session
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    implicit val mongoConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))
    // load product
    val productDF = spark.read.json(PRODUCT_PATH)
      .withColumnRenamed("asin", "productId")
      .select("productId", "categories", "description", "imUrl", "price", "title")
      .na.drop(Seq("productId", "title"))
      .cache()
    productDF.printSchema()
    saveToMongoDB(productDF, PRODUCT_COLLECTION, "productId")

    val ratingDF = spark.read.format("csv")
      .option("inferSchema", "true")
      .load(RATING_PATH)
      .toDF("userId", "productId", "rating", "timestamp")
      .join(productDF.select("productId"), Seq("productId"))
      .cache()
    ratingDF.printSchema()
    saveToMongoDB(ratingDF, RATING_COLLECTION, "userId")

//    val userDF = ratingDF
//      .select("userId")
//      .dropDuplicates()
//      .withColumn("password", lit("default"))
//      .cache()
//    userDF.printSchema()
//    println(userDF.count())
//    saveToMongoDB(userDF, USER_COLLECTION, "userId")

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
    logger.warn("Save %d data to mongoDB".format(df.count()))
  }
}
