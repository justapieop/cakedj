package me.justapie.cakedj.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.database.collections.ConfigCollection;
import me.justapie.cakedj.database.collections.GuildCollection;

public class DBConnector {
    private final MongoClient client;

    public DBConnector() {
        this.client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(
                                new ConnectionString(Dotenv.load().get(Constants.databaseAddressKey))
                        )
                        .retryWrites(true)
                        .build()
        );
    }

    public void initCollection() {
        MongoDatabase db = this.client.getDatabase(Constants.databaseName);
        ConfigCollection.init(db);
        GuildCollection.init(db);
    }
}
