package me.justapie.cakedj.command.commands.config;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.database.collections.GuildCollection;
import me.justapie.cakedj.database.models.GuildModel;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;

public class ChannelRestrictCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildModel guildSetting = GuildCollection.getGuildConfig(event.getGuild());
        if (guildSetting == null) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noConfigDefined);
            return;
        }
        if (guildSetting.djOnlyChannels().isEmpty()) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.emptyChannelList);
            return;
        }
        boolean enable = !guildSetting.channelRestrict();
        if (!event.getOptions().isEmpty())
            enable = event.getOptions().get(0).getAsBoolean();
        GuildCollection.modifyGuildConfig(event.getGuild(), Constants.channelRestrictKey, enable);
        String status = enable ? "on" : "off";
        String desc = "Turned " + status + " restrict mode";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("channelrestrict", "Enable or disable restrict mode")
                .addOption(OptionType.BOOLEAN, "enable", "Whether to enable restrict mode or not");
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.MANAGE_SERVER);
    }
}
