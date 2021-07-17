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

    public static final String filterReset = "All filters have been reset";

    public static final String cannotSelect = "Cannot select that page";

    public static final String databaseName = "CakeDJ";
    public static final String databaseAddressKey = "DATABASE";

    public static final String guildCollection = "guilds";

    public static final String ownerRequired = "You must be my owner to continue";

    public static final String configCollection = "config";
    public static final String noConfigDefined = "No config defined";
    public static final String tokenKey = "token";
    public static final String ownerIDKey = "ownerID";
    public static final String dblTokenKey = "dblToken";
    public static final String musicWithCakeDJ = "Music with CakeDJ";
    public static final String[] requiredScopes = new String[]{"bot", "applications.commands"};

    public static final String guildIDKey = "guildID";
    public static final String guildNameKey = "guildName";
    public static final String is247Key = "is247";
    public static final String channelRestrictKey = "channelRestrict";
    public static final String djOnlyChannelsKey = "djOnlyChannels";
    public static final String lavalinkNodeKey = "lavalinkNodes";

    public static final String queueTitle = "Queue";
    public static final String comingUp = "Coming up next";
    public static final String unrestrictedChannels = "Unrestricted channels";

    public static final String announcement = "Please use Discord's Slash Command system to execute commands";
    public static final String invite = "Click to invite me to your server";

    public static final String noMatches = "No tracks found";
    public static final String trackError = "Error while loading track. Please try again";
    public static final String trackSkipped = "Skipped playing track";
    public static final String trackPaused = "Track has been paused";
    public static final String trackAlreadyPaused = "This track has already been paused";
    public static final String trackResumed = "Track has been resumed";
    public static final String trackAlreadyResumed = "This track has already been resumed";
    public static final String queueCleared = "Queue cleared";
    public static final String noPrevTrack = "No previous tracks were found";
    public static final String channelJoined = "I have joined your channel and prepared to play music";
    public static final String channelLeft = "I left your voice channel";
    public static final String errorSwapping = "Cannot swap specified tracks";
    public static final String swapSuccessful = "Tracks have been swapped";
    public static final String errorRemoving = "Cannot remove specified tracks";
    public static final String removeSuccessful = "Track have been removed";
    public static final String parseError = "Parse error. Please enter position in `hms` format. Example: `2h3h4s`";
    public static final String numberParseError = "Invalid number";
    public static final String seeked = "Track has been seeked to the specified position";
    public static final String queueShuffled = "Queue has been shuffled";
    public static final String emptyChannelList = "Please add an unrestricted channel before turning on restrict mode";
    public static final String invalidChannelType = "Invalid channel type";

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
