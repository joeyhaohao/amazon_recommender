package edu.rice.cs.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "product")
@Getter
@Setter
@ToString
public class Product {

    @Id
    private String productId;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Text)
    private List<List<String>> categories;
    private String imUrl;
    private double price;
    private int ratingCount;
    private double ratingAvg;

}
