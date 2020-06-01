package ru.otus.pw01.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConfigurationProperties(prefix = "db")
@PropertySource("settings.yml")
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongoDBHost}")
    private String mongoDBHost;

    @Value("${mongoDBName}")
    private String mongoDBName;

    @Override
    public String getDatabaseName() {
        return mongoDBName;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(mongoDBHost);
    }

    @Bean("mongock-spring-boot")
    public Mongock mongock(MongoTemplate mongoTemplate) {
        return new SpringMongockBuilder(mongoTemplate, "ru.otus.pw01")
                .setLockQuickConfig()
                .build();
    }
}
