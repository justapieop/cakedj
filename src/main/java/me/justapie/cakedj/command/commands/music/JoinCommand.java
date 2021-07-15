package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.List;

public class JoinCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildVoiceState botState = event.getGuild().getSelfMember().getVoiceState();
        GuildVoiceState userState = event.getMember().getVoiceState();

        if (botState == null || userState == null) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.cannotDetectState);
            return;
        }

        AudioManager audioMan = event.getGuild().getAudioManager();

        if (!userState.inVoiceChannel()) {
            if (!(userState.getChannel() instanceof StageChannel)) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.noVoiceConnection);
                return;
            }
            StageChannel stage = (StageChannel) userState.getChannel();
            audioMan.openAudioConnection(stage);
        } else {
            if (!botState.inVoiceChannel()) {
                audioMan.openAudioConnection(userState.getChannel());
            } else if (botState.getChannel() != userState.getChannel()) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.noSameVoice);
                return;
            }
        }

        EmbedUtils.sendEmbed(event, Color.GREEN, Constants.channelJoined);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("join", "Join the voice or stage channel");
    }

    @Override
    public List<Permission> getUserPermissions() {
        return List.of(Permission.VOICE_CONNECT);
    }

    @Override
    public List<Permission> getBotPermission() {
        return List.of(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
    }
}
