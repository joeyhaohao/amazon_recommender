# amazon_recommender

## Dataset
The dataset is a part of [Amazon Review Data by Stanford](http://snap.stanford.edu/data/amazon/).
* user: 126k
* product: 45k
   * 5 categories: Automotive, Beauty, Office_Products, Software, Video_Games
* rating: 742k

## Frontend
* React
* Material-UI

## Backend
* Spring boot
* MongoDB
* Spark
* Kafka
* Redis
* ElasticSearch

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
