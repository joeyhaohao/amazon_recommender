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
   * title          Everyday Italian (with Giada de Laurentiis)...
   */
  case class Product(productId: String, categories: Array[String], description: String, imUrl: String,
                     price: Double, title: String)

  /**
   * Review
   * userId - ID of the reviewer, e.g. A2SUAM1J3GNN3B
   * productId - ID of the product, e.g. 0000013714
   * username - name of the reviewer
   * reviewText - text of the review
   * rating - rating of the product
   * summary - summary of the review
   * timestamp - time of the review (unix time)
   */
  case class Review(userId: String, productId: String, username: String, reviewText: String, rating: Double,
                    summary: String, timestamp: Long)

  /**
   * Rating
   * userId - ID of the user, e.g. A2SUAM1J3GNN3B
   * productId - ID of the product, e.g. 0000013714
   * rating - rating of the product
   * timestamp - time of the review (unix time)
   */
  case class Rating(userId: String, productId: String, rating: Double, timestamp: Long)

  // product recommendation object
  case class RecommendItem(productId: String, score: Double)

  // user recommendation list
  case class UserRecList(userId: String, recommendations: Seq[RecommendItem])

  // product similarity list
  case class ProductRecList(productId: String, recommendations: Seq[RecommendItem])
}
