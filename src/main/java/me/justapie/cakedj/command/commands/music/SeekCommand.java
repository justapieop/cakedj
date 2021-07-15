package me.justapie.cakedj.command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class SeekCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        String position = event.getOptions().get(0).getAsString();
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        long intPos = 0;

        try {
            intPos = Duration.parse("PT" + position).getSeconds() * 1000L;
        } catch (DateTimeParseException e) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.parseError);
            return;
        }
        player.getPlayingTrack().setPosition(intPos);
        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.seeked);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("seek", "Seek to specified position in the current track")
                .addOption(OptionType.STRING, "position", "Position to seek", true);
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activePlayer() {
        return true;
    }
}
