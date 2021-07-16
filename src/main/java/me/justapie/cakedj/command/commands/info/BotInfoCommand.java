package me.justapie.cakedj.command.commands.info;

import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.database.collections.ConfigCollection;
import me.justapie.cakedj.utils.EmbedUtils;
import me.justapie.cakedj.utils.TimeUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class BotInfoCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        SelfUser self = event.getJDA().getSelfUser();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Statistics")
                .setThumbnail(self.getAvatarUrl())
                .addField("OS", System.getProperty("os.name"), true)
                .addField("Java version", System.getProperty("java.version"), true)
                .addField("Owner", String.valueOf(event.getJDA().getUserById(ConfigCollection.getConfig().ownerID())), true)
                .addField("Bot created in", TimeUtils.formatDate(self.getTimeCreated()), true)
                .addField("Total servers", String.valueOf(event.getJDA().getGuilds().size()), true)
                .addField("Total shards", String.valueOf(event.getJDA().getShardManager().getShardsTotal()), true);
        EmbedUtils.sendEmbed(event, embed.build());
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("botinfo", "Show information about me");
    }
}
