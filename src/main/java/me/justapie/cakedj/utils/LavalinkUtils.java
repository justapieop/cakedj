package me.justapie.cakedj.utils;

import lavalink.client.io.jda.JdaLavalink;
import me.justapie.cakedj.database.collections.ConfigCollection;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.net.URI;
import java.net.URISyntaxException;

public class LavalinkUtils {
    public static JdaLavalink lavalink;

    public static void init(ShardManager shardManager) {
        lavalink = new JdaLavalink(
                shardManager.retrieveApplicationInfo().complete().getId(),
                shardManager.getShardsTotal(),
                shardManager::getShardById
        );

        if (!ConfigCollection.getConfig().rawNodeData().isEmpty()) {
            ConfigCollection.getConfig().nodes().forEach((key, val) -> {
                try {
                    lavalink.addNode(new URI(key), val);
                } catch (URISyntaxException ignored) {
                }
            });
        }

        shardManager.addEventListener(lavalink);
    }
}
