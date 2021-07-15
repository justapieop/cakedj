package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.audio.TrackScheduler;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class RestartCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
        scheduler.restart();
        EmbedUtils.sendEmbed(event, scheduler.getNowPlaying().build());
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("restart", "Restart the current track");
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
