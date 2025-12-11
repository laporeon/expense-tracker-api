package com.laporeon.expensetracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.laporeon.expensetracker.repository")
@EnableMongoAuditing
public class MongoConfiguration {
}
