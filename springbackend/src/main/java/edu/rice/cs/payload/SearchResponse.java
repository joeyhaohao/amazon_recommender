package edu.rice.cs.payload;

import edu.rice.cs.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SearchResponse {
    private List<Product> searchResult;
    private int page;
    private int totalPages;

}
