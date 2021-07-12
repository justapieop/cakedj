package me.justapie.cakedj.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface ICommand {
    void exec(SlashCommandEvent event);
    CommandData getCommandData();
    default List<Permission> getUserPermissions() { return List.of(); }
    default List<Permission> getBotPermission() { return List.of(); }
    default boolean isOwnerCommand() { return false; }
    default boolean needConnectedVoice() { return false; }
    default boolean sameVoice() { return false; }
    default boolean activeQueue() { return false; }
    default boolean activePlayer() { return false; }
}
