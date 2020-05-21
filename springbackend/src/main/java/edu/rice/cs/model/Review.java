package edu.rice.cs.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by joeyhaohao on 5/20/20
 */
@Entity
public class Review {

    private @Id
    @GeneratedValue
    Long id;
    private int userID;
    private int productID;
    private double rate;
    private String summary;
    private String text;
    private long timestamp;

    public Review(int userID, int productID, double rate, String summary, String text, long timestamp) {
        this.userID = userID;
        this.productID = productID;
        this.rate = rate;
        this.summary = summary;
        this.text = text;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return userID == review.userID &&
                productID == review.productID &&
                Double.compare(review.rate, rate) == 0 &&
                timestamp == review.timestamp &&
                Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, productID, rate, timestamp);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
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
                ", userID=" + userID +
                ", productID=" + productID +
                ", rate=" + rate +
                ", summary='" + summary + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
