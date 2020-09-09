# amazon_recommender
A collaborative filtering recommender system that delivers customized recommendations for online-shopping goods. It learns from customers recent events (browsing, searching, rating) and updates recommendation in real time.

## Getting Started
* Download the product metadata to `springbackend/src/main/resources/data`. Please see the data format in the next section.
* Download the user rating data to `recommender/src/main/resources/data`. Please see the data format in the next section.
* Use `springbackend/.../ProductLoader` to load the product metadata into Elasticsearch.
* Configure MongoDB connection in `application_properties`. Use `recommender/.../DataLoader` to load the rating data into MongoDB.
* Run `ALSRecommender, ItemCFRecommender, TrendingRecommmender` to train offline models.
* Install all service modules in Frontend and Backend sections.
* Build packages for Spring backend and online recommender.

    `cd springbackend && mvn clean install`
    
    `cd recommender && mvn clean install`
    
* Execute the script to start services (FE, BE, online recommender)

    `sh run_local.sh`
    
## Dataset
In this project the model is trained with part of the dataset [Amazon Review Data by Stanford](http://snap.stanford.edu/data/amazon/).
* User: 126k
* Product: 45k
   * 5 categories: Automotive, Beauty, Office_Products, Software, Video_Games
> {"asin":"B0000223J1","categories":[["Automotive","Tools & Equipment","Body Repair Tools","Buffing & Polishing Pads"]],"description":"This hook and loop polishing bonnet is an accessory for Makita polisher model 9227C.","imUrl":"http:\/\/ecx.images-amazon.com\/images\/I\/81E3GK0PEKL._SX300_.gif","price":14.4,"title":"Makita 743403-A Polishing Bonnet","ratingAvg":3.8333333333,"ratingCount":6}
* Rating: 742k
> {"userId":"A108J5O7DG2WIM","asin":B002YEY7EU,"rating":5,"timestamp":1382054400}

## Frontend
* React
* Material-UI

## Backend
* Spring Boot 2.3
* MongoDB 3.6
* Spark 2.2.0
* Kafka 2.4.1
* Redis 6.0.6
* ElasticSearch 7.8.1

## Recommender
### ALS Recommender ("Recommend For You")

Matrix factorization: factorize user-item rating matrix as the product of two lower dimensional metrics.
    
R = U * I, R: u×i, U: u×k, I: k×i (k: latent dimension)
    
![matrix factorization](https://github.com/joeyhaohao/amazon_recommender/blob/master/snapshots/matrix_factorization.png)
[source](https://towardsdatascience.com/prototyping-a-recommender-system-step-by-step-part-2-alternating-least-square-als-matrix-4a76c58714a1)
    
Since the model requires large dataset to train, it is trained offline daily with Spark ML. For new users who do not appear on the dataset, recommend top trending products from result of the Trending Recommender.
    
### Real-time Recommender ("Guess You Like")

This part of recommendation updates triggered by real-time customer events (e.g. browsing, rating, searching) on the website. Recommendation candidates are chosen from similar products of the event-related product.
    
![realtime recommender](https://github.com/joeyhaohao/amazon_recommender/blob/master/snapshots/realtime_recommender.png)
    
For new users who do not appear on the dataset, recommend top rated products from result of the Trending Recommender.
    
### Item-based CF Recommender ("People Also Like")
    
Recommend based on item similarity computed from previous user behavior (item A and item B are similar because they are liked by the same group by user)
    
![itemcf recommender](https://github.com/joeyhaohao/amazon_recommender/blob/master/snapshots/itemcf_recommender.png)

### Trending Recommender
* Trending products: the top 20 products with the most ratings in the past month.
* Top-rating products: the top 20 products with the highest average ratings of all time.
    
## Snapshot
![home](https://github.com/joeyhaohao/amazon_recommender/blob/master/snapshots/home_view.png)
![product](https://github.com/joeyhaohao/amazon_recommender/blob/master/snapshots/product_view.png)
![product](https://github.com/joeyhaohao/amazon_recommender/blob/master/snapshots/search_view.png)
