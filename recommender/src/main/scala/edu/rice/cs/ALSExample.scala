package edu.rice.cs

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.SparkSession

/**
 * An example demonstrating ALS.
 * Run with
 * {{{
 * bin/run-example ml.ALSExample
 * }}}
 */
object ALSExample{

  case class Rating(userId: Int, movieId: Int, rating: Float, timestamp: Long) extends Serializable
  def parseRating(str: String): Rating = {
    val fields = str.split("::")
    assert(fields.size == 4)
    Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toLong)
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .config("spark.master", "local")
      .appName("ALSExample")
      .getOrCreate()

    import spark.implicits._
    val ratings = spark.read.textFile("recommender/src/resources/sample_movielens.txt")
      .map(parseRating)
      .toDF()
    val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

    // Build the recommendation model using ALS on the training data
    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating")
    val model = als.fit(training)

    // Evaluate the model by computing the RMSE on the test data
    // Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
    model.setColdStartStrategy("drop")
    val predictions = model.transform(test)

    val evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")
    val rmse = evaluator.evaluate(predictions)
    println(s"Root-mean-square error = $rmse")

    // Generate top 10 movie recommendations for each user
    val userRecs = model.recommendForAllUsers(10).toDF()
    // Generate top 10 user recommendations for each movie
    val movieRecs = model.recommendForAllItems(10)

    userRecs.printSchema()
    val tmp = userRecs.where("userId == 0").select("recommendations.movieId", "recommendations.rating").collect()
    println(tmp)
//    // Generate top 10 movie recommendations for a specified set of users
//    val users = ratings.select(als.getUserCol).distinct().limit(3)
//    // Generate top 10 user recommendations for a specified set of movies
//    val movies = ratings.select(als.getItemCol).distinct().limit(3)
//    // $example off$
//    userRecs.show()
//    movieRecs.show()

    spark.stop()
  }
}
