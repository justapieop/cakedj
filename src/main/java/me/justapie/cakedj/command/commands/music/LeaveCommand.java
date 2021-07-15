package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class LeaveCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        event.getGuild().getAudioManager().closeAudioConnection();
        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.channelLeft);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("leave", "Leave the voice or stage channel");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }
}
