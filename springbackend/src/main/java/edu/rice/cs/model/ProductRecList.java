package edu.rice.cs.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRecList {
    private String productId;
    private List<RecommendItem> recommendations;

    public ProductRecList(String productId, List<RecommendItem> recommendations) {
        this.productId = productId;
        this.recommendations = recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRecList that = (ProductRecList) o;
        return productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<RecommendItem> getRecList() {
        return recommendations;
    }

    public void setRecList(ArrayList<RecommendItem> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public String toString() {
        return "RecommendResult{" +
                "productId='" + productId + '\'' +
                ", recList=" + recommendations +
                '}';
    }
}
