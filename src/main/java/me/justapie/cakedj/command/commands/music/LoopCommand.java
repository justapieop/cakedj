package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.audio.TrackScheduler;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class LoopCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
        boolean enable = !scheduler.isInLoop;
        if (!event.getOptions().isEmpty())
            enable = event.getOptions().get(0).getAsBoolean();
        scheduler.setInLoop(enable);
        String status = enable ? "on" : "off";
        String desc = "Turned " + status + " queue looping";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("loop", "Toggle loop for the queue")
                .addOption(OptionType.BOOLEAN, "enable", "Whether to enable or disable loop");
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
