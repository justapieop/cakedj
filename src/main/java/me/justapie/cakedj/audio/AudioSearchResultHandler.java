package me.justapie.cakedj.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.justapie.cakedj.utils.DiscordMarkdown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;

public final class AudioSearchResultHandler implements AudioLoadResultHandler {
    private final GuildMusicManager manager;
    private final User requester;
    private final InteractionHook hook;
    private final String trackUrl;
    private final Guild guild;

    public AudioSearchResultHandler(GuildMusicManager manager, InteractionHook hook, String trackUrl) {
        this.manager = manager;
        this.hook = hook;
        this.trackUrl = trackUrl;
        this.requester = this.hook.getInteraction().getUser();
        this.guild = this.hook.getInteraction().getGuild();
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        track.setUserData(this.requester.getAsTag());
        this.manager.scheduler.enqueue(track);
        this.hook.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setTitle("Enqueued track")
                        .setDescription(track.getInfo().title)
                        .addField("Requested by", this.requester.getAsTag(), false)
                        .setAuthor(this.guild.getName(), null, this.guild.getIconUrl())
                        .build()
        ).queue();
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if (trackUrl.startsWith("scsearch:")) {
            AudioTrack track = playlist.getTracks().get(0);
            track.setUserData(this.requester.getAsTag());
            this.manager.scheduler.enqueue(track);
            this.hook.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setTitle("Enqueued track")
                            .setDescription(track.getInfo().title)
                            .addField("Requested by", this.requester.getAsTag(), false)
                            .setAuthor(this.guild.getName(), null, this.guild.getIconUrl())
                            .build()
            ).queue();
            return;
        }

        for (AudioTrack track : playlist.getTracks()) {
            track.setUserData(this.requester.getAsTag());
            this.manager.scheduler.enqueue(track);
        }

        this.hook.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setTitle("Playlist enqueued")
                        .setDescription(playlist.getName())
                        .addField("Requested by", this.requester.getAsTag(), false)
                        .setFooter("Playlist size: " + playlist.getTracks().size())
                        .setAuthor(this.guild.getName(), null, this.guild.getIconUrl())
                        .build()
        ).queue();
    }

    @Override
    public void noMatches() {
        this.hook.sendMessageEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(DiscordMarkdown.bold("Load failed"))
                .setDescription("The requested track couldn't be loaded")
                .build()
        ).queue();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        this.hook.sendMessageEmbeds(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(DiscordMarkdown.bold("Not found"))
                .setDescription("The requested track couldn't be found")
                .build()
        ).queue();
    }
}
