package me.justapie.cakedj.command.commands.filter;

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class BassboostCommand implements ICommand {

    private static final float[] freqGain = {-0.05f, 0.07f, 0.16f, 0.03f, -0.05f, -0.11f};

    @Override
    public void exec(SlashCommandEvent event) {
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer;
        float multiplier;
        try {
            multiplier = Float.parseFloat(event.getOptions().get(0).getAsString());
        } catch (NumberFormatException e) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.numberParseError);
            return;
        }

        if (multiplier == 1) {
            player.setFilterFactory(null);
        } else {
            EqualizerFactory factory = new EqualizerFactory();

            for (int i = 0; i < freqGain.length; i++)
                factory.setGain(i, freqGain[i] * multiplier);

            player.setFilterFactory(factory);
        }

        String desc = "Set bassboost multiplier to `" + multiplier + "x` This might take a few seconds";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("bassboost", "Boost your music bass")
                .addOption(OptionType.STRING, "multiplier", "Bass multiplier", true);
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
