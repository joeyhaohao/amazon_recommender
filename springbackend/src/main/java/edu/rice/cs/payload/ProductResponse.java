package edu.rice.cs.payload;

import edu.rice.cs.model.Product;
import edu.rice.cs.model.Rating;

import java.util.List;

public class ProductResponse {
    private Product product;
    private List<Rating> ratingList;
    private int ratingCount;
    private double ratingAvg;

    public ProductResponse(Product product, List<Rating> ratingList) {
        this.product = product;
        this.ratingList = ratingList;
        this.ratingCount = ratingList.size();
        double sum = 0;
        for (Rating rating: ratingList) {
            sum += rating.getRating();
        }
        this.ratingAvg = this.ratingCount == 0? 0: sum / this.ratingCount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public double getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(double ratingAvg) {
        this.ratingAvg = ratingAvg;
    }
}
