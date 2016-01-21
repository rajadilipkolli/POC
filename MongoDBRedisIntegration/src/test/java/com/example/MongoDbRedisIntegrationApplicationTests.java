package com.example;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MongoDbRedisIntegrationApplication.class)
@WebAppConfiguration
public class MongoDbRedisIntegrationApplicationTests {

    @Autowired
    BookRepository repository;
    
    @Autowired WebServicesController controller;
    
	@Test
	public void contextLoads() {
	}

	@Test
	public void insertData() {
	    repository.deleteAll();
	    Book book = new Book();
	    book.setTitle("MongoDbCookBook");
	    book.setText("MongoDB Data Book");
	    book.setAuthor("Raja");
	    Book response = controller.saveBook(book);
	    assertNotNull(response);
	}
}
