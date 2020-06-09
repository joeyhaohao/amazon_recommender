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
   * Product
   * productId      0000143561
   * categories     [['Movies & TV', 'Movies']]
   * description    3Pack DVD set - Italian Classics, Parties and Holidays.
   * imUrl          http://g-ecx.images-amazon.com/images/G/01/x-site/icons/no-img-sm._CB192198896_.gif
   * price          12.99
   * ralated
   * salesRank
   * title          Everyday Italian (with Giada de Laurentiis)...
   */
  case class Product(productId: String, categories: Array[String], description: String, imUrl: String,
                     price: Double, title: String)

  /**
   * Review
   * productId          0000143561
   * helpful            [0, 0]
   * rate               4.0
   * reviewText         This is a charming version of the classic Dicken's tale...
   * reviewTime         02 26, 2008
   * userId             ADZPIG9QOCDG5
   * username           Alice L. Larson \"alice-loves-books\"
   * summary            good version of a classic
   * overall            4.0
   * timestamp          1203984000
   */
  case class Review(productId: String, rate: Double, reviewText: String, reviewTime: String,
                    userId: String, username: String, summary: String, timestamp: Long)

  // product recommendation object
  case class RecommendItem(productId: String, score: Double)

  // user recommendation list
  case class UserRecList(userId: String, recommendations: Seq[RecommendItem])

  // product similarity list
  case class ProductRecList(productId: String, recommendations: Seq[RecommendItem])
}
