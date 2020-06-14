package edu.rice.cs.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "als_recommendation")
public class UserRecList {
    private String userId;
    private List<RecommendItem> recommendations;

    public UserRecList(String userId, List<RecommendItem> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecList that = (UserRecList) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
                "userId='" + userId + '\'' +
                ", recList=" + recommendations +
                '}';
    }
}
