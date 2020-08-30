package edu.rice.cs.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.rice.cs.model.Product;
import edu.rice.cs.repositories.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication(scanBasePackages={"edu.rice.cs"})
public class ProductLoader {

    private static Logger logger = LogManager.getLogger(ProductLoader.class.getName());

    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProductLoader.class, args).close();
    }

//    @Bean
//    // load data to Elasticsearch
//    CommandLineRunner runner() {
//        return args -> {
//            int count = 0;
//            ObjectMapper mapper = new ObjectMapper();
//            TypeReference<Product> typeReference = new TypeReference<Product>(){};
//            InputStream inputStream = TypeReference.class.getResourceAsStream("/data/meta_5cat.json");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                try {
//                    line = line.replace("asin", "productId");
//                    Product product = mapper.readValue(line, typeReference);
//                    productRepository.save(product);
//                    count ++;
//                } catch (IOException e){
//                    logger.info("Unable to save products: " + e.getMessage());
//                }
//            }
//            logger.info(String.format("Saved %d products to Elasticsearch", count));
//        };
//    }
}
