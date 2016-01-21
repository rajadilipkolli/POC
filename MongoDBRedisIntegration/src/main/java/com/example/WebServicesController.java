package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/book")
public class WebServicesController
{

    @Autowired
    BookRepository repository;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @RequestMapping(value = "/saveBook", method = RequestMethod.POST)
    public Book saveBook(Book book)
    {
        Book inserted = repository.save(book);
        return inserted;
    }

    @RequestMapping(value = "/findByTitle/{title}", method = RequestMethod.GET)
    @Cacheable(value="book", key="#title")
    public Book findBookByTitle(@PathVariable String title)
    {
        Book value = (Book) redisTemplate.opsForHash().get("BOOK", title);
        if (null != value)
        {
            return value;
        }
        List<Book> inserted = repository.findByTitle(title);
        if (inserted.isEmpty())
        {
            return null;
        }
        redisTemplate.opsForHash().put("BOOK", title, inserted.get(0));
        return inserted.get(0);
    }
}
