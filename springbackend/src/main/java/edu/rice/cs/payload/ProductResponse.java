package edu.rice.cs.payload;

import edu.rice.cs.model.Product;
import edu.rice.cs.model.Rating;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponse {
    private Product product;
    private int ratingCount;
    private double ratingAvg;

    public ProductResponse(Product product, List<Rating> ratingList) {
        this.product = product;
        this.ratingCount = ratingList.size();
        double sum = 0;
        for (Rating rating: ratingList) {
            sum += rating.getRating();
        }
        this.ratingAvg = this.ratingCount == 0? 0: sum / this.ratingCount;
    }

}
