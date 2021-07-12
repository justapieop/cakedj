package me.justapie.cakedj.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager instance;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandEvent event, String trackUrl, User requester) {
        if (event.getGuild() == null) return;

        GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(requester.getAsTag());
                musicManager.scheduler.enqueue(track);
                String desc = "Enqueued" + ' ' + "**" + track.getInfo().title + "**" +
                        '.' + ' ' +
                        "Requested by" + ' ' + "**" + requester.getAsTag() + "**";
                EmbedUtils.sendEmbed(event, Color.GREEN, desc);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (trackUrl.startsWith("ytsearch:")) {
                    AudioTrack track = playlist.getTracks().get(0);
                    track.setUserData(requester.getAsTag());
                    musicManager.scheduler.enqueue(track);
                    String desc = "Enqueued" + ' ' + "**" + track.getInfo().title + "**" +
                            '.' + ' ' +
                            "Requested by" + ' ' + "**" + requester.getAsTag() + "**";
                    EmbedUtils.sendEmbed(event, Color.GREEN, desc);
                    return;
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });

    }

    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();

        return instance;
    }
}
