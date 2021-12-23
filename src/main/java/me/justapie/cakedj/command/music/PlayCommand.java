package me.justapie.cakedj.command.music;

import me.justapie.cakedj.Context;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.structure.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class PlayCommand extends Command {
    private static final String YOUTUBE_REGEX = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";

    public PlayCommand() {
        this.data = new CommandData("play", "Play music")
                .addOption(OptionType.STRING, "name", "URL or song name", true)
                .addOption(OptionType.BOOLEAN, "force", "Force start the track and push the playing track into the queue");
        this.sameVoice = true;
        this.userPerms = List.of(Permission.VOICE_CONNECT);
        this.botPerms = List.of(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.REQUEST_TO_SPEAK);
    }

    @Override
    public void execute(Context context) {
        String songName = context.getOptions().get(0).getAsString();
        if (songName.matches(YOUTUBE_REGEX)) {
            context.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("Unsupported url")
                            .setDescription("Youtube urls are unsupported due to recent shutdowns of music bots")
                            .build()
            ).queue();
            return;
        }
        try {
            new URI(songName);
        } catch (URISyntaxException e) {
            songName = "scsearch:" + songName;
        }
        PlayerManager.getInstance().loadAndPlay(context, context.getInteraction().getUser(), songName);
    }
}
