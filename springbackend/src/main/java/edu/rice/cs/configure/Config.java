package edu.rice.cs.configure;

//import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class Config {
    /************** MONGODB ****************/
    public static String MONGODB_DATABASE = "recommender";
    public static String MONGODB_USER_COLLECTION = "user";
    public static String MONGODB_USER_REC_COLLECTION = "user_recommendation";
    public static String MONGODB_PRODUCT_REC_COLLECTION = "product_recommendation";

    public static String MONGODB_HOST = "127.0.0.1";
    public static Integer MONGODB_PORT = 27017;

//    public static String KAFKA_HOST = "127.0.0.1";
//    public static Integer KAFKA_PORT = 9092;
//    public static String KAFKA_TOPIC = null;

//    public static String REDIS_HOST = "127.0.0.1";
//    public static Integer REDIS_PORT = 6379;

//    @Bean(name = "mongoClient")
//    public MongoClient getMongoClient() {
//        MongoClient mongoClient = new MongoClient(MONGODB_HOST, MONGODB_PORT);
//        return mongoClient;
//    }

    static {
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("application.properties");
        try {
            properties.load(new FileInputStream(resource.getFile()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("properties file not found");
        }

        MONGODB_HOST = properties.getProperty("mongo.host");
        MONGODB_PORT = Integer.parseInt(properties.getProperty("mongo.port"));

//        KAFKA_HOST = properties.getProperty("kafka.host");
//        KAFKA_PORT = Integer.parseInt(properties.getProperty("kafka.port"));
//        KAFKA_TOPIC = properties.getProperty("kafka.topic");

//        REDIS_HOST = properties.getProperty("redis.host");
//        REDIS_PORT = Integer.parseInt(properties.getProperty("redis.port"));
    }
}
