package me.justapie.cakedj.command.commands.music;

import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.ICommand;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

public class VolumeCommand implements ICommand {
    @Override
    public void exec(SlashCommandEvent event) {
        int volume = Integer.parseInt(event.getOptions().get(0).getAsString());
        PlayerManager.getInstance().getMusicManager(event.getGuild()).audioPlayer.setVolume(volume);
        String desc = "Volume has been set to `" + volume + "%`";
        EmbedUtils.sendEmbed(event, Color.GREEN, desc);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("volume", "Set the volume of the playing track")
                .addOption(OptionType.INTEGER, "volume", "Volume to set", true);
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
