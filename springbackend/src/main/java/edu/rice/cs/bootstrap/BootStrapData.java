//package edu.rice.cs.bootstrap;
//
//import edu.rice.cs.model.Product;
//import edu.rice.cs.repositories.ProductRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
///**
// * Created by songxiongfeng on 5/21/20
// */
//@Component
//public class BootStrapData implements CommandLineRunner {
//    private final ProductRepository productRepository;
//
//    public BootStrapData(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("Started in Bootstrap");
//
//        Product product1 = new Product();
//        product1.setProductID("123");
//        product1.setProductName("ABC");
//        product1.setCategory("Sci");
//        product1.setImageUrl("https://google.com");
//
//        productRepository.save(product1);
//
//        System.out.println("Number of Products: " + productRepository.count());
//    }
//}
