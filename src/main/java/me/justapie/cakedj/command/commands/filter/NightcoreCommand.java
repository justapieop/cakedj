package me.justapie.cakedj.command.commands.filter;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.Collections;

public class NightcoreCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        GuildMusicManager musicMan = PlayerManager.getInstance().getMusicManager(event.getGuild());
        boolean enable = !musicMan.scheduler.isNightcore;
        if (!event.getOptions().isEmpty())
            enable = event.getOptions().get(0).getAsBoolean();
        musicMan.audioPlayer.setFilterFactory(null);
        if (enable) {
            musicMan.audioPlayer.setFilterFactory((track, format, output) -> {
                TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                timescale.setSpeed(1.3);
                timescale.setPitch(1.25);
                return Collections.singletonList(timescale);
            });
        }
        musicMan.scheduler.setNightcore(enable);
        String status = enable ? "on" : "off";
        String desc = "Turned " + status + " nightcore. This might take a few seconds";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("nightcore", "Transform your music into a nightcore version")
                .addOption(OptionType.BOOLEAN, "enable", "Whether to enable nightcore or not");
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
