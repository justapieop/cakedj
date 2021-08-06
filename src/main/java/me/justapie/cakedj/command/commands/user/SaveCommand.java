package me.justapie.cakedj.command.commands.user;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.database.collections.UserCollection;
import me.justapie.cakedj.database.models.AudioTrackMin;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildMusicManager musicMan = PlayerManager.getInstance().getMusicManager(event.getGuild());
        List<AudioTrack> queue = musicMan.scheduler.queue;

        List<AudioTrackMin> savedTracks = new ArrayList<>();

        AudioTrack playing = musicMan.audioPlayer.getPlayingTrack();

        savedTracks.add(
                new AudioTrackMin() {
                    @Override
                    public String title() {
                        return playing.getInfo().title;
                    }

                    @Override
                    public String uri() {
                        return playing.getInfo().uri;
                    }
                }
        );

        for (AudioTrack track : queue) {
            AudioTrackMin minTrack = new AudioTrackMin() {
                @Override
                public String title() {
                    return track.getInfo().title;
                }

                @Override
                public String uri() {
                    return track.getInfo().uri;
                }
            };

            savedTracks.add(minTrack);
        }

        List<List<AudioTrackMin>> playlists = UserCollection.getUserData(event.getUser()).playlists();

        playlists.add(savedTracks);

        UserCollection.modifyUserConfig(event.getUser(), Constants.playlistKey, playlists);

        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.playlistSaved);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("save", "Save the current queue as a playlist");
    }

    @Override
    public boolean activeQueue() {
        return true;
    }

    @Override
    public boolean activePlayer() {
        return true;
    }
}
