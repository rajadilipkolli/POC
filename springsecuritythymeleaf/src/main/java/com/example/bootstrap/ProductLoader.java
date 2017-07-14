package com.example.bootstrap;

import java.math.BigDecimal;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.example.domain.Product;
import com.example.repositories.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class ProductLoader implements ApplicationListener<ContextRefreshedEvent> {
 
    private ProductRepository productRepository;
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        productRepository.deleteAll();
 
        Product shirt = new Product();
        shirt.setDescription("Spring Framework Guru Shirt");
        shirt.setPrice(new BigDecimal("18.95"));
        shirt.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_shirt-rf412049699c14ba5b68bb1c09182bfa2_8nax2_512.jpg");
        shirt.setProductId("235268845711068308");
        productRepository.save(shirt);
        
        log.info("Saved Shirt - id: " + shirt.getId());
 
        Product mug = new Product();
        mug.setDescription("Spring Framework Guru Mug");
        mug.setImageUrl("https://springframework.guru/wp-content/uploads/2015/04/spring_framework_guru_coffee_mug-r11e7694903c348e1a667dfd2f1474d95_x7j54_8byvr_512.jpg");
        mug.setProductId("168639393495335947");
        productRepository.save(mug);
        
        log.info("Saved Mug - id:" + mug.getId());
    }
}
