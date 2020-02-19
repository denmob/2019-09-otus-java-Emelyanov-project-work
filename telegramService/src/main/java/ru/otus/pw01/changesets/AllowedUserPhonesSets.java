package ru.otus.pw01.changesets;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.pw01.config.AllowedUserPhonesConfig;

@ChangeLog
@Component
public class AllowedUserPhonesSets {
    private static final String COLLECTION_NAME = "AllowedUserPhones";

    @Bean
    public AllowedUserPhonesSets getAllowedUserPhonesSets(ApplicationContext context) {
        allowedUserPhonesConfig = context.getBean(AllowedUserPhonesConfig.class);
        return this;
    }

    @Autowired
    private static AllowedUserPhonesConfig allowedUserPhonesConfig;

    @ChangeSet(id= "withMongoDatabase01", order = "001", author = "Mongock")
    public void changeSet1(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(createMongoDocument(allowedUserPhonesConfig.getAllowUserPhoneNumber1()));
    }

    @ChangeSet(id= "withMongoDatabase02", order = "002", author = "Mongock")
    public void changeSet2(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(createMongoDocument(allowedUserPhonesConfig.getAllowUserPhoneNumber2()));
    }

    @ChangeSet(id= "withMongoDatabase03", order = "003", author = "Mongock")
    public void changeSet3(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(createMongoDocument(allowedUserPhonesConfig.getAllowUserPhoneNumber3()));
    }

    private Document createMongoDocument(String phoneNumber) {
        return new Document()
                .append("AllowUserPhoneNumber", phoneNumber);
    }
}
