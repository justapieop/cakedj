package me.justapie.cakedj.database.collections;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.database.models.ConfigModel;
import org.bson.Document;
import org.slf4j.LoggerFactory;

public class ConfigCollection {
    private static MongoCollection<Document> collection;
    public static void init(MongoDatabase database) {
        collection = database.getCollection(Constants.configCollection);
    }

    public static ConfigModel getConfig() {
        Document document = collection.find().first();

        if (document == null) {
            LoggerFactory.getLogger(ConfigCollection.class).info(Constants.noConfigDefined);
            System.exit(0);
        }

        return new ConfigModel() {
            @Override
            public String token() {
                return document.getString(Constants.tokenKey);
            }

            @Override
            public String ownerID() {
                return document.getString(Constants.ownerIDKey);
            }

            @Override
            public String dblToken() {
                return document.getString(Constants.dblTokenKey);
            }
        };
    }
}
