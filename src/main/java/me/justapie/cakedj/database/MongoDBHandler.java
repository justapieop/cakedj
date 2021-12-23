package me.justapie.cakedj.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.justapie.cakedj.CakeDJ;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public final class MongoDBHandler {
    private static final CodecRegistry CODEC_REGISTRY = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(
                    PojoCodecProvider.builder().automatic(true).build()
            )
    );
    private static final MongoClientSettings CLIENT_SETTINGS = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(CakeDJ.getConfig().getDatabaseUrl()))
            .applicationName("CakeDJ")
            .retryWrites(true)
            .retryReads(true)
            .writeConcern(WriteConcern.MAJORITY)
            .codecRegistry(CODEC_REGISTRY)
            .build();
    private static final MongoClient CLIENT = MongoClients.create(CLIENT_SETTINGS);

    public static synchronized MongoClient getClient() {
        return CLIENT;
    }
}
