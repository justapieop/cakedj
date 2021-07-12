package me.justapie.cakedj.command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.TimeUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class NowPlayingCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        EmbedBuilder npEmbed = manager.scheduler.getNowPlaying();
        AudioTrack playing = manager.audioPlayer.getPlayingTrack();
        npEmbed.addField(" ", this.getProgressBar(playing), false);
        event.deferReply().addEmbeds(npEmbed.build()).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("nowplaying", "Now playing...");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activePlayer() {
        return true;
    }

    public String getProgressBar(AudioTrack track) {
        float percentage = (100f / track.getDuration() * track.getPosition());
        return "`" + TimeUtils.formatTime(track.getPosition()) + "` [" + "▬".repeat((int) Math.round((double) percentage / 10)) +
                "](https://github.com/justapieop/cakedj)" +
                "▬".repeat(10 - (int) Math.round((double) percentage / 10)) +
                " `" + TimeUtils.formatTime(track.getDuration()) + "`";
    }
}
