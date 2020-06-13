package edu.rice.cs.config;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfig {

    @Value(value = "${spring.data.mongodb.uri}")
    private String uri;

    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create(uri);
        return mongoClient;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoClient(), "test");
        return template;
    }
}
