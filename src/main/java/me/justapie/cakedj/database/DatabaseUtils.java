package me.justapie.cakedj.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.justapie.cakedj.database.model.GuildSetting;
import net.dv8tion.jda.api.entities.Guild;

public final class DatabaseUtils {
    private static final MongoClient CLIENT = MongoDBHandler.getClient();
    private static final MongoDatabase DATABASE = CLIENT.getDatabase("CakeDJ");

    public static GuildSetting getGuildSetting(Guild guild) {
        MongoCollection<GuildSetting> collection = DATABASE.getCollection("guilds", GuildSetting.class);
        return collection.find(Filters.eq("guildId", guild.getId())).first();
    }

    public static void updateData(Guild guild, GuildSetting setting) {
        MongoCollection<GuildSetting> collection = DATABASE.getCollection("guilds", GuildSetting.class);
        deleteSetting(guild);
        collection.insertOne(setting);
    }

    public static void createSetting(Guild guild) {
        MongoCollection<GuildSetting> collection = DATABASE.getCollection("guilds", GuildSetting.class);
        collection.insertOne(
                new GuildSetting()
                        .setGuildId(guild.getId())
                        .setGuildName(guild.getName())
        );
    }

    public static void deleteSetting(Guild guild) {
        MongoCollection<GuildSetting> collection = DATABASE.getCollection("guilds", GuildSetting.class);
        collection.findOneAndDelete(Filters.eq("guildId", guild.getId()));
    }
}
