package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Product;
import com.example.email.EmailService;
import com.example.repositories.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThymeleafSecurityApplicationTests {

    private ProductRepository productRepository;
    @Autowired private EmailService emailService;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    

    @Test
    public void testSendEmail()
    {
        emailService.sendEmail("rajadilipkolli@gmail.com", "JCart - Test Mail", "This is a test email from JCart");
    } 

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSaveProduct() {
        // setup product
        Product product = new Product();
        product.setDescription("Spring Framework Shirt");
        product.setPrice(new BigDecimal("18.95"));
        product.setProductId("1234");

        // save product, verify has ID value after save
        assertNull(product.getId()); // null before save
        productRepository.save(product);
        assertNotNull(product.getId()); // not null after save

        // fetch from DB
        Product fetchedProduct = productRepository.findOne(product.getId());

        // should not be null
        assertNotNull(fetchedProduct);

        // should equal
        assertEquals(product.getId(), fetchedProduct.getId());
        assertEquals(product.getDescription(), fetchedProduct.getDescription());

        // update description and save
        fetchedProduct.setDescription("New Description");
        productRepository.save(fetchedProduct);

        // get from DB, should be updated
        Product fetchedUpdatedProduct = productRepository.findOne(fetchedProduct.getId());
        assertEquals(fetchedProduct.getDescription(),
                fetchedUpdatedProduct.getDescription());

        // verify count of products in DB
        long productCount = productRepository.count();
        assertEquals(productCount, 1);

        // get all products, list should only have one
        List<Product> products = productRepository.findAll();

        assertEquals(products.size(), 1);
    }
}
