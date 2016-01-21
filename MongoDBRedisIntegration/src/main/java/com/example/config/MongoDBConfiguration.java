package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class MongoDBConfiguration extends AbstractMongoConfiguration
{

    @Override
    protected String getDatabaseName()
    {
        return "test";
    }

    @Override
    public Mongo mongo() throws Exception
    {
        return new MongoClient("127.0.0.1");
    }

}
