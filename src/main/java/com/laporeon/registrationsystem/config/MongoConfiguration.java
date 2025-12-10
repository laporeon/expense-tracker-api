package com.laporeon.registrationsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.laporeon.registrationsystem.repository")
@EnableMongoAuditing
public class MongoConfiguration {
}
