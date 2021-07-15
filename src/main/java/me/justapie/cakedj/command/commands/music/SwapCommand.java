package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.audio.TrackScheduler;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class SwapCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        int a = Integer.parseInt(event.getOptions().get(0).getAsString()) - 1;
        int b = Integer.parseInt(event.getOptions().get(1).getAsString()) - 1;
        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
        try {
            scheduler.swap(a, b);
        } catch (IndexOutOfBoundsException e) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.errorSwapping);
            return;
        }
        EmbedUtils.sendEmbed(event, Color.RED, Constants.swapSuccessful);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("swap", "Swap position of two tracks")
                .addOption(OptionType.INTEGER, "a", "First track true", true)
                .addOption(OptionType.INTEGER, "b", "Second track position", true);
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
