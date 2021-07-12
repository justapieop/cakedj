package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.command.ICommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SkipCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {

    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("skip", "Skip current track");
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
