package me.justapie.cakedj.command;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private static final List<ICommand> commands = new ArrayList<>();
    public static final List<CommandData> commandData = new ArrayList<>();

    public Manager() {

    }

    private static void addCommand(ICommand command) {
        commands.add(command);
        commandData.add(command.getCommandData());
    }
}
