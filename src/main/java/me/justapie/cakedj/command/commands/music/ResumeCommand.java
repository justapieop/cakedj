package me.justapie.cakedj.command.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class ResumeCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        if (!player.isPaused()) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.trackAlreadyResumed);
            return;
        }
        player.setPaused(false);
        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.trackResumed);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("resume", "Resume the current track");
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
