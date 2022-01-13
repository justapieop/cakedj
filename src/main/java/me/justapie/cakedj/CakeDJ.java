package me.justapie.cakedj;

import com.google.common.base.Splitter;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import me.justapie.cakedj.structure.BotConfig;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public final class CakeDJ {
    private static final Properties PROPERTIES = new Properties();
    private static final List<GatewayIntent> REQUIRED_INTENTS = List.of(
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_VOICE_STATES
    );
    private static final List<CacheFlag> DISABLED_FLAG = List.of(
            CacheFlag.EMOTE,
            CacheFlag.ACTIVITY,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.ONLINE_STATUS
    );

    static {
        try {
            PROPERTIES.load(CakeDJ.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BotConfig getConfig() {
        return new BotConfig() {
            @Override
            public String getToken() {
                return PROPERTIES.getProperty("token");
            }

            @Override
            public List<String> getOwnerIds() {
                String ownersStr = PROPERTIES.getProperty("owners").trim();
                Splitter mySplitter = Splitter.on(',')
                        .trimResults()
                        .omitEmptyStrings();
                return mySplitter.splitToList(ownersStr);
            }

            @Override
            public String getDatabaseUrl() {
                return PROPERTIES.getProperty("databaseUrl");
            }

            @Override
            public String geniusKey() {
                return PROPERTIES.getProperty("geniusKey");
            }

            @Override
            public String spotifyClientId() {
                return PROPERTIES.getProperty("spotifyClientId");
            }

            @Override
            public String spotifyClientSecret() {
                return PROPERTIES.getProperty("spotifyClientSecret");
            }
        };
    }

    public void start() throws LoginException {
        DefaultShardManagerBuilder
                .create(REQUIRED_INTENTS)
                .setToken(CakeDJ.getConfig().getToken())
                .disableCache(DISABLED_FLAG)
                .addEventListeners(new Listener())
                .setAudioSendFactory(new NativeAudioSendFactory())
                .build();
    }

    public static void main(String[] args) throws LoginException, IOException {
        new CakeDJ().start();
    }
}
