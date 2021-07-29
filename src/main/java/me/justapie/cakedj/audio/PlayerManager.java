package me.justapie.cakedj.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.lava.extensions.youtuberotator.YoutubeIpRotatorSetup;
import com.sedmelluq.lava.extensions.youtuberotator.planner.NanoIpRoutePlanner;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.database.collections.ConfigCollection;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.slf4j.LoggerFactory;

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

        if (!ConfigCollection.getConfig().ipv6Block().isEmpty()) {
            new YoutubeIpRotatorSetup(
                    new NanoIpRoutePlanner(
                            ConfigCollection.getConfig().ipv6Block(), true
                    )
            ).forSource(
                    audioPlayerManager.source(YoutubeAudioSourceManager.class)
            ).setup();
        }
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandEvent event, String trackUrl, User requester) {
        GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        InteractionHook hook = event.deferReply().complete();
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(requester.getAsTag());
                musicManager.scheduler.enqueue(track);
                String desc = "Enqueued" + ' ' + "**" + track.getInfo().title + "**" + "\n" +
                        "Requested by" + ' ' + "**" + requester.getAsTag() + "**";
                hook.sendMessageEmbeds(
                        EmbedUtils.createEmbed(Color.GREEN, desc)
                ).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (trackUrl.startsWith("ytsearch:")) {
                    AudioTrack track = playlist.getTracks().get(0);
                    track.setUserData(requester.getAsTag());
                    musicManager.scheduler.enqueue(track);
                    String desc = "Enqueued" + ' ' + "**" + track.getInfo().title + "**" + "\n" +
                            "Requested by" + ' ' + "**" + requester.getAsTag() + "**";
                    hook.sendMessageEmbeds(
                            EmbedUtils.createEmbed(Color.GREEN, desc)
                    ).queue();
                    return;
                }

                for (AudioTrack track : playlist.getTracks()) {
                    track.setUserData(requester.getAsTag());
                    musicManager.scheduler.enqueue(track);
                }

                int size = playlist.getTracks().size();

                String desc = "Enqueued **" + size + "** " + (size > 1 ? "tracks" : "track") + " from playlist **" + playlist.getName() + "**" + "\n"
                        + "Requested by **" + requester.getAsTag() + "**";

                hook.sendMessageEmbeds(
                        EmbedUtils.createEmbed(Color.GREEN, desc)
                ).queue();
            }

            @Override
            public void noMatches() {
                hook.sendMessageEmbeds(
                        EmbedUtils.createEmbed(Color.RED, Constants.noMatches)
                ).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                hook.sendMessageEmbeds(
                        EmbedUtils.createEmbed(Color.RED, Constants.trackError)
                ).queue();
                LoggerFactory.getLogger(PlayerManager.class).error(
                        exception.getMessage()
                );
            }
        });

    }

    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();

        return instance;
    }
}
