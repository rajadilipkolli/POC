package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Book;
import com.example.repository.BookRepository;

@RestController
@RequestMapping(value = "/book")
public class WebServicesController
{

    @Autowired
    BookRepository repository;

   /* @Autowired
    RedisTemplate<Object, Object> redisTemplate;*/

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/saveBook", method = RequestMethod.POST)
    public Book saveBook(Book book)
    {
        return repository.save(book);
    }

    @RequestMapping(value = "/findByTitle/{title}", method = RequestMethod.GET)
    @Cacheable(value = "book", key = "#title")
    public Book findBookByTitle(@PathVariable String title)
    {
        /*Book value = (Book) redisTemplate.opsForHash().get("BOOK", title);
        if (null != value)
        {
            return value;
        }*/
        Book insertedBook = repository.findByTitle(title);
//            redisTemplate.opsForHash().put("BOOK", title, insertedBook);
        return insertedBook;
    }

    @RequestMapping(value = "/updateByTitle/{title}/{author}", method = RequestMethod.GET)
    @CachePut(value = "book", key = "#title")
    public Book updateByTitle(@PathVariable(value = "title") String title,
            @PathVariable(value = "author") String author)
    {
        Query query = new Query(Criteria.where("title").is(title));
        Update update = new Update().set("author", author);
        Book result = mongoTemplate.findAndModify(query, update,
                new FindAndModifyOptions().returnNew(true).upsert(false), Book.class);
//        redisTemplate.opsForHash().put("BOOK", title, result);
        return result;
    }
}
