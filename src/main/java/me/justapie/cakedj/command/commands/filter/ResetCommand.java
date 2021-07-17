package me.justapie.cakedj.command.commands.filter;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class ResetCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.setFilterFactory(null);
        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.filterReset);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("reset", "Reset all filters");
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
