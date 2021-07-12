package me.justapie.cakedj.command.commands.music;

import com.google.common.collect.Lists;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.List;

public class QueueCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        List<AudioTrack> queue = manager.scheduler.queue;
        if (queue.isEmpty()) {
            if (manager.audioPlayer.getPlayingTrack() != null) {
                event.deferReply().addEmbeds(manager.scheduler.getNowPlaying().build()).queue();
                return;
            }
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noActiveQueue);
            return;
        }
        List<List<AudioTrack>> paginatedQueue = Lists.partition(queue, 10);
        int selected = 0;
        if (!event.getOptions().isEmpty())
            selected = Integer.parseInt(event.getOptions().get(0).getAsString()) - 1;
        if (selected >= paginatedQueue.size() || selected < 0) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.cannotSelect);
            return;
        }
        List<AudioTrack> selectedQueue = paginatedQueue.get(selected);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(Constants.queueTitle);
        builder.setColor(Color.GREEN);
        builder.setDescription(Constants.comingUp);
        for (AudioTrack track : selectedQueue) {
            String info = "`" + (queue.indexOf(track) + 1) + ' ' +
                    track.getInfo().title +
                    '`';
            builder.addField(info, "Requested by **" + track.getUserData(String.class) + "**", false);
        }
        event.deferReply().addEmbeds(builder.build()).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("queue", "Show the queue")
                .addOption(OptionType.INTEGER, "page", "Show the queue from a specified page");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activeQueue() {
        return true;
    }

    @Override
    public boolean activePlayer() {
        return true;
    }
}
