package com.example;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class WebServicesController
{

    @Autowired
    BookRepository repository;
    
    @Autowired
    private RedisTemplate<String,Book> redisTemplate;

    @RequestMapping(name="/saveBook", method = RequestMethod.POST)
    public String saveBook(Book book)
    {
        Book inserted = repository.save(book);
        return inserted.getId();
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(name="/findByTitle/{title}", method = RequestMethod.GET)
    @Cacheable(value = "BOOK", key = "#id" ) 
    public Optional<Book> findBookByTitle(@PathVariable String title)
    {
        Optional<Book> value = (Optional<Book>)redisTemplate.opsForHash().get("BOOK", title);
        if (null != value)
        {
            return value;
        }
        Stream<Book> inserted = repository.findByTitle(title);
        redisTemplate.opsForHash().put("BOOK", title, inserted.findFirst());
        return inserted.findFirst();
    }
}
