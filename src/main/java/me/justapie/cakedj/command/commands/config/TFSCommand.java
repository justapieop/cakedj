package me.justapie.cakedj.command.commands.config;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.database.collections.GuildCollection;
import me.justapie.cakedj.database.models.GuildModel;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class TFSCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildModel guildSetting = GuildCollection.getGuildConfig(event.getGuild());
        if (guildSetting == null) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noConfigDefined);
            return;
        }
        boolean enable = !guildSetting.is247();
        if (!event.getOptions().isEmpty())
            enable = event.getOptions().get(0).getAsBoolean();
        GuildCollection.modifyGuildConfig(event.getGuild(), Constants.is247Key, enable);
        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.setInLoop(enable);
        String status = enable ? "on" : "off";
        String desc = "Turned " + status + " 24/7 mode";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("tfs", "Make the player play music 24/7")
                .addOption(OptionType.BOOLEAN, "enable", "Whether to enable 24/7 mode or not");
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
