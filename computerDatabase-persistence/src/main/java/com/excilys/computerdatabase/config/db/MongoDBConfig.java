package com.excilys.computerdatabase.config.db;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

@Configuration
public class MongoDBConfig {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MongoDBConfig.class);

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(), "computerdatabase-db");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

        return mongoTemplate;

    }

    @PostConstruct
    public void initApp() {
        LOGGER.info("MongodB configuring...");
    }
}
