package me.justapie.cakedj;

import me.justapie.cakedj.command.music.LyricsCommand;
import me.justapie.cakedj.command.music.PlayCommand;
import me.justapie.cakedj.structure.Command;
import me.justapie.cakedj.utils.DiscordMarkdown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandManager {
    private final List<Command> commandList = new ArrayList<>();
    public final List<CommandData> dataList = new ArrayList<>();

    public CommandManager() {
        addCommand(new PlayCommand());
        addCommand(new LyricsCommand());
    }

    private void addCommand(Command command) {
        if (!this.commandList.contains(command)) {
            this.commandList.add(command);
            this.dataList.add(command.data);
        }
    }

    private Command getCommand(String cmdName) {
        for (Command command : this.commandList) {
            if (command.data.getName().equals(cmdName)) return command;
        }
        return null;
    }

    public void process(SlashCommandEvent event) {
        event.deferReply().queue((hook) -> {
            Context context = new Context(hook, event);
            Command cmd = this.getCommand(event.getName());
            if (cmd != null && this.checkRequirements(hook, cmd)) cmd.execute(context);
        });
    }

    private boolean checkRequirements(InteractionHook hook, Command command) {
        Member commander = hook.getInteraction().getMember();
        assert commander != null;
        if (!command.userPerms.isEmpty()) {
            List<String> missing = command.userPerms.stream()
                    .filter((c) -> !hook.getInteraction().getMember().hasPermission(c))
                    .map(Permission::getName)
                    .collect(Collectors.toList());
            if (!missing.isEmpty()) {
                hook.sendMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setTitle("Insufficient permissions")
                                .setDescription("You don't have enough permissions to do this")
                                .addField("Missing permissions", String.join(", ", missing), true)
                                .build()
                ).queue();
                return false;
            }
        }

        if (!command.botPerms.isEmpty()) {
            List<String> missing = command.userPerms.stream()
                    .filter((c) -> !hook.getInteraction().getGuild().getSelfMember().hasPermission(c))
                    .map(Permission::getName)
                    .collect(Collectors.toList());
            if (!missing.isEmpty()) {
                hook.sendMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setTitle("Insufficient permissions")
                                .setDescription("The bot doesn't have enough permissions to do this")
                                .addField("Missing permissions", String.join(", ", missing), true)
                                .build()
                ).queue();
                return false;
            }
        }

        if (command.sameVoice) {
            AudioManager audioManager = hook.getInteraction().getGuild().getAudioManager();
            GuildVoiceState state = hook.getInteraction().getMember().getVoiceState();
            if (!audioManager.isConnected()) {
                if (state.inAudioChannel()) {
                    AudioChannel channel = state.getChannel();
                    if (channel instanceof StageChannel) {
                        StageChannel stage = (StageChannel) channel;
                        audioManager.openAudioConnection(stage);
                    } else audioManager.openAudioConnection(channel);
                } else {
                    hook.sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setTitle("No voice connection detected")
                                    .setDescription("You must be connected to a voice channel to continue")
                                    .build()
                    ).queue();
                    return false;
                }
            } else {
                if (state.inAudioChannel()) {
                    if (!state.getChannel().equals(audioManager.getConnectedChannel())) {
                        hook.sendMessageEmbeds(
                                new EmbedBuilder()
                                        .setColor(Color.RED)
                                        .setTitle("Same voice required")
                                        .setDescription("You must be connected to the same voice/stage channel with the bot to continue")
                                        .addField("Current voice/stage channel", DiscordMarkdown.singleQuote(audioManager.getConnectedChannel().getName()), true)
                                        .build()
                        ).queue();
                        return false;
                    }
                } else {
                    hook.sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setTitle("No voice connection detected")
                                    .setDescription("You must be connected to a voice channel to continue")
                                    .build()
                    ).queue();
                    return false;
                }
            }
        }
        return true;
    }
}
