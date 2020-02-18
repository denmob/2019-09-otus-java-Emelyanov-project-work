package ru.otus.pw01.changesets;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


@ChangeLog
public class AllowedUserPhonesSets {
    private static final String COLLECTION_NAME = "AllowedUserPhones";

    @ChangeSet(id= "withMongoDatabase01", order = "001", author = "Mongock")
    public void changeSet1(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(createMongoDocument("+38093718891"));
    }

    @ChangeSet(id= "withMongoDatabase02", order = "002", author = "Mongock")
    public void changeSet2(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(createMongoDocument("38093718891"));
    }

    @ChangeSet(id= "withMongoDatabase03", order = "003", author = "Mongock")
    public void changeSet3(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(COLLECTION_NAME).insertOne(createMongoDocument("093718891"));
    }

    private Document createMongoDocument(String phoneNumber) {
        return new Document()
                .append("AllowUserPhoneNumber", phoneNumber);
    }
}
