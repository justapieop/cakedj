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

public class RemoveCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        int a = Integer.parseInt(event.getOptions().get(0).getAsString());
        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
        if (a <= 0 || a >= scheduler.queue.size()) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.errorRemoving);
            return;
        }
        try {
            scheduler.queue.remove(a - 1);
        } catch (IndexOutOfBoundsException e) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.errorRemoving);
            return;
        }
        EmbedUtils.sendEmbed(event, Color.RED, Constants.removeSuccessful);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("remove", "Remove selected track")
                .addOption(OptionType.INTEGER, "position", "Track position", true);
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
