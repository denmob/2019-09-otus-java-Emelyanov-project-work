package ru.otus.pw01.changesets;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.pw01.config.AllowedUserConfig;

@ChangeLog
@Component
public class AllowedUserSets {
    private static final String COLLECTION_NAME = "allowed_user";

    @Bean
    public AllowedUserSets getAllowedUserPhonesSets(ApplicationContext context) {
        allowedUserConfig = context.getBean(AllowedUserConfig.class);
        return this;
    }

    private static AllowedUserConfig allowedUserConfig;

    @ChangeSet(id= "withMongoDatabase01", order = "001", author = "Mongock")
    public void changeSet1(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(
                createMongoDocument(allowedUserConfig.getName1(), allowedUserConfig.getPhone1()));
    }

    @ChangeSet(id= "withMongoDatabase02", order = "002", author = "Mongock")
    public void changeSet2(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(
                createMongoDocument(allowedUserConfig.getName2(), allowedUserConfig.getPhone2()));
    }

    @ChangeSet(id= "withMongoDatabase03", order = "003", author = "Mongock")
    public void changeSet3(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(
                createMongoDocument(allowedUserConfig.getName3(), allowedUserConfig.getPhone3()));
    }

    private Document createMongoDocument(String userName, String phoneNumber) {
        return new Document()
                .append("user_name", userName)
                .append("phone_number", phoneNumber);
    }
}
