package me.justapie.cakedj.command.commands.config;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.database.collections.GuildCollection;
import me.justapie.cakedj.database.models.GuildModel;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.List;

public class ChannelManageCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildModel guildSetting = GuildCollection.getGuildConfig(event.getGuild());
        if (guildSetting == null) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noConfigDefined);
            return;
        }
        List<String> djOnlyChannels = guildSetting.djOnlyChannels();
        if (event.getSubcommandName().equals("add"))
            this.subCommandAdd(event, djOnlyChannels);
        else if (event.getSubcommandName().equals("remove"))
            this.subCommandRemove(event, djOnlyChannels);
        else if (event.getSubcommandName().equals("list"))
            this.subCommandList(event, djOnlyChannels);
    }

    private void subCommandAdd(SlashCommandEvent event, List<String> djOnlyChannels) {
        GuildChannel channel = event.getOptions().get(0).getAsGuildChannel();
        if (!channel.getType().isMessage()) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.invalidChannelType);
            return;
        }
        djOnlyChannels.add(channel.getId());
        GuildCollection.modifyGuildConfig(event.getGuild(), Constants.djOnlyChannelsKey, djOnlyChannels);
        String desc = "Added channel `" + channel.getName() + "` to unrestricted channels list";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    private void subCommandRemove(SlashCommandEvent event, List<String> djOnlyChannels) {
        GuildChannel channel = event.getOptions().get(0).getAsGuildChannel();
        if (!channel.getType().isMessage()) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.invalidChannelType);
            return;
        }
        djOnlyChannels.remove(channel.getId());
        GuildCollection.modifyGuildConfig(event.getGuild(), Constants.djOnlyChannelsKey, djOnlyChannels);
        String desc = "Removed channel `" + channel.getName() + "` from unrestricted channels list";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    private void subCommandList(SlashCommandEvent event, List<String> djOnlyChannels) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(Constants.unrestrictedChannels);
        builder.setColor(Color.CYAN);
        String desc = "";
        for (String id : djOnlyChannels) {
            GuildChannel channel = event.getGuild().getGuildChannelById(id);
            if (channel == null) continue;
            desc += "`" + (djOnlyChannels.indexOf(channel.getId()) + 1) + "` <#" + channel.getId() + ">\n";
        }
        builder.setDescription(desc);
        EmbedUtils.sendEmbed(event, builder.build());
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("channelmanage", "Manage unrestricted channels")
                .addSubcommands(
                        new SubcommandData("add", "Add a channel")
                                .addOption(OptionType.CHANNEL, "channel", "Channel to add", true),
                        new SubcommandData("remove", "Remove a channel")
                                .addOption(OptionType.CHANNEL, "channel", "Channel to remove", true),
                        new SubcommandData("list", "A list of unrestricted channels")
                );
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.MANAGE_SERVER);
    }
}
