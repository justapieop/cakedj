package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class ShuffleCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.shuffle();
        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.queueShuffled);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("shuffle", "Shuffle the queue");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activeQueue() {
        return true;
    }
}
