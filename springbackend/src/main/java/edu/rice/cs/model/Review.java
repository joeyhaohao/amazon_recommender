package edu.rice.cs.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.util.Objects;

/**
 * Created by joeyhaohao on 5/20/20
 */
@Document(collection = "review")
public class Review {

    @Id
    private String id;
    @Indexed
    private String userId;
    @Indexed
    private String productId;
    private double rate;
    private long timestamp;
    private String summary;
    private String text;

    public Review(String userId, String productId, double rate, long timestamp) {
        this.userId = userId;
        this.productId = productId;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id &&
                userId == review.userId &&
                productId == review.productId &&
                Double.compare(review.rate, rate) == 0 &&
                timestamp == review.timestamp &&
                Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userId, rate, timestamp);
    }

    public String getId() {
        return id;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userID=" + userId +
                ", productID=" + productId +
                ", rate=" + rate +
                ", summary='" + summary + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
