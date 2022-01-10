package me.justapie.cakedj.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.justapie.cakedj.CakeDJ;

public final class MongoDBHandler {
    private static final MongoClientSettings CLIENT_SETTINGS = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(CakeDJ.getConfig().getDatabaseUrl()))
            .applicationName("CakeDJ")
            .retryWrites(true)
            .retryReads(true)
            .writeConcern(WriteConcern.MAJORITY)
            .build();
    private static final MongoClient CLIENT = MongoClients.create(CLIENT_SETTINGS);

    public static synchronized MongoClient getClient() {
        return CLIENT;
    }
}
