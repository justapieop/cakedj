package me.justapie.cakedj.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.HashMap;
import java.util.Map;

public final class PlayerManager {
    private static PlayerManager instance;
    private final Map<String, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getId(), (guildId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(InteractionHook hook, User requester, String url) {
        GuildMusicManager guildMusicManager = this.getMusicManager(hook.getInteraction().getGuild());
        this.audioPlayerManager.loadItemOrdered(
                guildMusicManager,
                url,
                new AudioSearchResultHandler(guildMusicManager, hook, requester, url)
        );
    }

    public static PlayerManager getInstance() {
        if (instance == null) instance = new PlayerManager();
        return instance;
    }
}
