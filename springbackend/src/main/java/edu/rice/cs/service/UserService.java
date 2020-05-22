package edu.rice.cs.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import edu.rice.cs.configure.Config;
import edu.rice.cs.model.Product;
import edu.rice.cs.model.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private ObjectMapper objectMapper;

    private MongoCollection<Document> userCollection;

    public UserService() {
        userCollection = mongoClient.getDatabase(Config.MONGODB_DATABASE).getCollection(Config.MONGODB_USER_COLLECTION);
    }

    public User getUserByID(String userID) {
        Document userDoc = userCollection.find(new Document("userId", userID)).first();
        if (userDoc == null || userDoc.isEmpty())
            return null;

        User user = null;
        try {
            user = objectMapper.readValue(JSON.serialize(userDoc), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

}
