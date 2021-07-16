package me.justapie.cakedj.command.commands.info;

import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class PingCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        event.getJDA().getRestPing().queue((ping) -> EmbedUtils.sendEmbed(
                event,
                Color.GREEN,
                String.format(
                        "🏓 Ping: %dms\n⚜ WS Ping: %dms", ping, event.getJDA().getGatewayPing()
                )
        ));
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("ping", "Show the ping between the bot and the server");
    }
}
