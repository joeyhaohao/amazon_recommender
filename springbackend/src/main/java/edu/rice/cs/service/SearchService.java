package edu.rice.cs.service;

import edu.rice.cs.model.Product;
import edu.rice.cs.payload.SearchRequest;
import edu.rice.cs.repositories.ProductRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private static final int DEFAULT_SIZE = 10;

    @Autowired
    private ProductRepository productRepository;

//    @Autowired
//    private ElasticsearchTemplate template;

    public List<Product> search(SearchRequest request) {
        int pageInd = request.getPage() - 1;

        // send a query to Elasticsearch
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(pageInd, DEFAULT_SIZE))
                .withQuery(QueryBuilders.matchQuery("title", request.getKeyword()));
        Page<Product> result = productRepository.search(queryBuilder.build());
//        AggregatedPage<ProductES> result = template.queryForPage(queryBuilder.build(), ProductES.class);

//        long total = result.getTotalElements();
//        Integer totalPages1 = result.getTotalPages();
//        Long totalPages = total % size == 0 ? total / size : total / size + 1;
        List<Product> goodsList = result.getContent();

        return goodsList;
    }
}
