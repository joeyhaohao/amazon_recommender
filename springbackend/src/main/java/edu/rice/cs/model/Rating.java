package edu.rice.cs.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.Objects;

/**
 * Created by joeyhaohao on 6/14/20
 */
@Document(collection = "rating")
public class Rating {

    @Indexed
    private String userId;
    @Indexed
    private String productId;
    private double rating;
    private long timestamp;

    public Rating(String userId, String productId, double rating, long timestamp) {
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating1 = (Rating) o;
        return Double.compare(rating1.rating, rating) == 0 &&
                timestamp == rating1.timestamp &&
                Objects.equals(userId, rating1.userId) &&
                Objects.equals(productId, rating1.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId, rating, timestamp);
    }

    @Override
    public String toString() {
        return "Rating{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}
