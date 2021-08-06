package me.justapie.cakedj.command.commands.user;

import me.justapie.cakedj.command.ICommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class PlaylistCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {

    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("playlist", "Manage your playlists")
                .addSubcommands(
                        new SubcommandData("view", "View your playlists")
                                .addOption(OptionType.INTEGER, "playlist", "Select the playlist you want to view"),
                        new SubcommandData("create", "Create a new playlist"),
                        new SubcommandData("add", "Add a track into your playlist"),
                        new SubcommandData("remove", "")
                );
    }
}
