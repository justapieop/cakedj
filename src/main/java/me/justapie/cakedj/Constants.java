package me.justapie.cakedj;

import me.justapie.cakedj.database.models.GuildModel;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;

import java.util.List;

public class Constants {
    public static final String noPerms = "You don't have permission to do this";
    public static final String noBotPerms = "I don't have enough permissions to do this";
    public static final String noVoiceConnection = "You must connect to a voice or a stage channel first";
    public static final String noSameVoice = "You must connect to the same voice or a stage channel with the bot first";
    public static final String noActivePlayer = "There aren't any track being played";
    public static final String noActiveQueue = "The queue is empty. Please add a track first";
    public static final String noCommandFound = "No command found";
    public static final String cannotDetectState = "Cannot detect your voice state";

    public static final String cannotSelect = "Cannot select that page";

    public static final String databaseName = "CakeDJ";
    public static final String databaseAddressKey = "DATABASE";

    public static final String configCollection = "config";
    public static final String noConfigDefined = "No config defined";
    public static final String tokenKey = "token";
    public static final String ownerIDKey = "ownerID";
    public static final String dblTokenKey = "DBLToken";
    public static final String musicWithCakeDJ = "Music with CakeDJ";

    public static final String guildIDKey = "guildID";
    public static final String guildNameKey = "guildName";
    public static final String is247Key = "is247";
    public static final String channelRestrictKey = "channelRestrict";
    public static final String djOnlyChannelsKey = "djOnlyChannels";


    public static final String queueTitle = "Queue";
    public static final String comingUp = "Coming up next";

    public static final String noMatches = "No tracks found";
    public static final String trackError = "Error while loading track. Please try again";
    public static final String trackSkipped = "Skipped playing track";

    public static Document getDefaultGuildSetting(Guild guild) {
        return new Document()
                .append(guildNameKey, guild.getName())
                .append(guildIDKey, guild.getId())
                .append(is247Key, false)
                .append(channelRestrictKey, false)
                .append(djOnlyChannelsKey, List.of());
    }

    public static GuildModel parseGuildSetting(Document document) {
        return new GuildModel() {

            @Override
            public String guildName() {
                return document.getString(guildNameKey);
            }

            @Override
            public String guildID() {
                return document.getString(guildIDKey);
            }

            @Override
            public boolean is247() {
                return document.getBoolean(is247Key);
            }

            @Override
            public boolean channelRestrict() {
                return document.getBoolean(channelRestrictKey);
            }

            @Override
            public List<String> djOnlyChannels() {
                return document.getList(djOnlyChannelsKey, String.class);
            }
        };
    }
}
