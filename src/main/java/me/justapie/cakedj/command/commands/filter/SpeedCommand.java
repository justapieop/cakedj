package me.justapie.cakedj.command.commands.filter;

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.Collections;

public class SpeedCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        float speed = 1F;
        if (!event.getOptions().isEmpty()) {
            try {
                speed = Float.parseFloat(event.getOptions().get(0).getAsString());
            } catch (NumberFormatException e) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.numberParseError);
                return;
            }
        }
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        player.setFilterFactory(null);
        if (speed > 1) {
            float finalSpeed = speed;
            player.setFilterFactory((track, format, output) -> {
                TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                timescale.setSpeed(finalSpeed);
                return Collections.singletonList(timescale);
            });
        }
        String desc = "Set the speed of current track to `" + speed + "x` This might take a few seconds";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("speed", "Set the speed of current track")
                .addOption(OptionType.STRING, "multiplier", "Speed multiplier");
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
