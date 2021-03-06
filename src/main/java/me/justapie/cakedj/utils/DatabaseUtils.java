package me.justapie.cakedj.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.justapie.cakedj.database.MongoDBHandler;
import org.bson.Document;

import java.util.Arrays;

public final class DatabaseUtils {
    private static final MongoClient CLIENT = MongoDBHandler.getClient();
    private static final MongoDatabase DATABASE = CLIENT.getDatabase("CakeDJ");

    public static Document getDocument(String collName, String key, Object val) {
        MongoCollection<Document> collection = DATABASE.getCollection(collName);
        return collection.find(Filters.eq(key, val)).first();
    }

    public static void updateDocument(String collName, String key, Object val, Document setting) {
        MongoCollection<Document> collection = DATABASE.getCollection(collName);
        deleteDocument(collName, key, val);
        collection.insertOne(setting);
    }

    public static void createDocument(String collName, Document... doc) {
        MongoCollection<Document> collection = DATABASE.getCollection(collName);
        collection.insertMany(Arrays.asList(doc));
    }

    public static void deleteDocument(String collName, String key, Object val) {
        MongoCollection<Document> collection = DATABASE.getCollection(collName);
        collection.deleteMany(Filters.eq(key, val));
    }
}
