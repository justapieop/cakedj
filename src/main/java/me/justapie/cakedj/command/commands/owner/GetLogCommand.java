package me.justapie.cakedj.command.commands.owner;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.database.collections.ConfigCollection;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.io.File;

public class GetLogCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        User user = event.getJDA().getUserById(ConfigCollection.getConfig().ownerID());
        assert user != null;
        PrivateChannel channel = user.openPrivateChannel().complete();
        File logFile = new File("./log.txt");
        if (!logFile.exists() || !logFile.canRead() || !logFile.isFile()) {
            EmbedUtils.sendEmbed(event, Color.GREEN, Constants.noLog, true);
            return;
        }
        try {
            channel.sendFile(logFile).queue();
        } catch (InsufficientPermissionException e) {
            EmbedUtils.sendEmbed(event, Color.GREEN, Constants.cannotSend, true);
            return;
        }
        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.logSent, true);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("getlog", "Retrieve the log of the bot");
    }

    @Override
    public boolean isOwnerCommand() {
        return true;
    }
}
