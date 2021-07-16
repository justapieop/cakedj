package me.justapie.cakedj.command.commands.info;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class InviteCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        String link = event.getJDA()
                .setRequiredScopes(Constants.requiredScopes)
                .getInviteUrl(
                        Permission.VOICE_CONNECT,
                        Permission.VOICE_SPEAK,
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_READ,
                        Permission.MESSAGE_HISTORY,
                        Permission.USE_SLASH_COMMANDS,
                        Permission.VOICE_MUTE_OTHERS
                );
        EmbedUtils.sendEmbed(event, Color.GREEN,
                "[" + Constants.invite + "](" + link + ")"
        );
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("invite", "Click to invite your bot to your server");
    }
}
