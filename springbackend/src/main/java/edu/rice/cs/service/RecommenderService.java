package edu.rice.cs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.rice.cs.configure.Config;
import edu.rice.cs.model.Product;

@Service
public class RecommenderService {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Product> getUserProductRec(String userID, int num) {
        MongoCollection<Document> userRecCollection = mongoClient.getDatabase(Config.MONGODB_DATABASE).getCollection(Config.MONGODB_USER_REC_COLLECTION);
        Document userRecDoc = userRecCollection.find(new Document("userID", userID)).first();
        List<Product> products = new ArrayList<>();
        if (userRecDoc != null) {
            ArrayList<Document> productRecs = userRecDoc.get("recommendations", ArrayList.class);
            List<Integer> productIds = new ArrayList<>();
            for (Document rec : productRecs) {
                productIds.add(rec.getInteger("productId"));
            }
            // find all products in recommendation list
            FindIterable<Document> productDocs = userRecCollection.find(Filters.in("productId", productIds));
            for (Document prodDoc : productDocs) {
                try {
                    Product product = objectMapper.readValue(JSON.serialize(prodDoc), Product.class);
                    products.add(product);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return products;
    }

}
