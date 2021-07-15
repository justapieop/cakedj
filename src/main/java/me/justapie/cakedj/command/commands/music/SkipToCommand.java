package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.audio.TrackScheduler;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SkipToCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        int a = Integer.parseInt(event.getOptions().get(0).getAsString());
        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler;
        for (int i = 0; i < a - 1; i++)
            scheduler.queue.remove();
        scheduler.endTrack();
        EmbedUtils.sendEmbed(event, scheduler.getNowPlaying().build());
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("skipto", "Skip to selected track")
                .addOption(OptionType.INTEGER, "a", "Position of the track");
    }

    @Override
    public boolean sameVoice() {
        return true;
    }

    @Override
    public boolean activeQueue() {
        return true;
    }
}
