package me.justapie.cakedj.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.justapie.cakedj.database.model.GuildSetting;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

public final class DatabaseUtils {
    private static final MongoClient CLIENT = MongoDBHandler.getClient();
    private static final MongoDatabase DATABASE = CLIENT.getDatabase("CakeDJ");

    public static Document getDocument(Guild guild) {
        MongoCollection<Document> collection = DATABASE.getCollection("guilds");
        return collection.find(Filters.eq("guildId", guild.getId())).first();
    }

    public static void updateData(String guildId, Document setting) {
        MongoCollection<Document> collection = DATABASE.getCollection("guilds");
        deleteSetting(guildId);
        collection.insertOne(setting);
    }

    public static void createSetting(String guildId, String guildName) {
        MongoCollection<Document> collection = DATABASE.getCollection("guilds");
        collection.insertOne(
                new Document()
                        .append("guildId", guildId)
                        .append("guildName", guildName)
        );
    }

    public static void deleteSetting(String guildId) {
        MongoCollection<GuildSetting> collection = DATABASE.getCollection("guilds", GuildSetting.class);
        collection.findOneAndDelete(Filters.eq("guildId", guildId));
    }
}
