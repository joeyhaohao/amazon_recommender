package edu.rice

package object cs {
  /**
   * MongoDB connection configuration
   *
   * @param uri MongoDB connection uri
   * @param db  database name
   */
  case class MongoConfig(uri: String, db: String)

  /**
   * rating data
   * reviewerID         ADZPIG9QOCDG5
   * productID          0005019281
   * summary            good version of a classic
   * reviewText         This is a charming version of the classic Dicken's tale...
   * overall            4.0
   * timestamp          1203984000
   */
  case class Review(reviewerID: String, asin: String, summary: String, reviewText: String,
                    overall: Double, unixReviewTime: Long)
}
