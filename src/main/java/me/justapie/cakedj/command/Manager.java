package me.justapie.cakedj.command;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.command.commands.music.*;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private final List<ICommand> commands = new ArrayList<>();
    public final List<CommandData> commandData = new ArrayList<>();

    public Manager() {
        this.addCommand(
                new PlayCommand(),
                new QueueCommand(),
                new NowPlayingCommand(),
                new SkipCommand(),
                new PauseCommand(),
                new ResumeCommand(),
                new ClearCommand()
        );
    }

    private void addCommand(ICommand... cmd) {
        for (ICommand command : cmd) {
            this.commands.add(command);
            this.commandData.add(command.getCommandData());
        }
    }

    private ICommand getCommand(String search) {
        for (ICommand command : this.commands) {
            if (command.getCommandData().getName().equalsIgnoreCase(search))
                return command;
        }
        return null;
    }

    public void process(SlashCommandEvent event) {
        ICommand command = this.getCommand(event.getName());
        if (command == null) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noCommandFound);
            return;
        }

        command.exec(event);
    }
}
