package edu.rice.cs.model;

import java.util.Objects;

public class RecommendItem {
    private String productId;
    private Double score;

    public RecommendItem(String productId, Double score) {
        this.productId = productId;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendItem that = (RecommendItem) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, score);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "RecommendItem{" +
                "productId='" + productId + '\'' +
                ", score=" + score +
                '}';
    }
}
