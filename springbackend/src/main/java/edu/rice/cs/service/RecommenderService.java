package edu.rice.cs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommenderService {

    @Autowired
    private ObjectMapper objectMapper;

//    public List<Product> getUserProductRec(String userID, int num) {
//        MongoCollection<Document> userRecCollection = mongoClient.getDatabase(Config.MONGODB_DATABASE).getCollection(Config.MONGODB_USER_REC_COLLECTION);
//        Document userRecDoc = userRecCollection.find(new Document("userID", userID)).first();
//        List<Product> products = new ArrayList<>();
//        if (userRecDoc != null) {
//            ArrayList<Document> productRecs = userRecDoc.get("recommendations", ArrayList.class);
//            List<Integer> productIds = new ArrayList<>();
//            for (Document rec : productRecs) {
//                productIds.add(rec.getInteger("productId"));
//            }
//            // find all products in recommendation list
//            FindIterable<Document> productDocs = userRecCollection.find(Filters.in("productId", productIds));
//            for (Document prodDoc : productDocs) {
//                try {
//                    Product product = objectMapper.readValue(JSON.serialize(prodDoc), Product.class);
//                    products.add(product);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return products;
//    }

//    public RecommendList getRealtimeRecommendation(String userId) {
//        return null;
//    }

}
