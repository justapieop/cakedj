package me.justapie.cakedj.command;

import me.justapie.cakedj.Constants;
import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.commands.music.*;
import me.justapie.cakedj.database.collections.ConfigCollection;
import me.justapie.cakedj.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private final List<ICommand> commands = new ArrayList<>();
    public final List<CommandData> commandData = new ArrayList<>();

    public Manager() {
        this.addCommand(
                new PlayCommand(),
                new QueueCommand(),
                new NowPlayingCommand(),
                new SkipCommand(),
                new PauseCommand(),
                new ResumeCommand(),
                new ClearCommand(),
                new PreviousCommand(),
                new JoinCommand(),
                new LeaveCommand(),
                new SwapCommand(),
                new RemoveCommand(),
                new LoopCommand(),
                new RestartCommand(),
                new SeekCommand(),
                new VolumeCommand(),
                new ShuffleCommand(),
                new SkipToCommand()
        );
    }

    private void addCommand(ICommand... cmd) {
        for (ICommand command : cmd) {
            this.commands.add(command);
            this.commandData.add(command.getCommandData());
        }
    }

    private ICommand getCommand(String search) {
        for (ICommand command : this.commands) {
            if (command.getCommandData().getName().equalsIgnoreCase(search))
                return command;
        }
        return null;
    }

    public void process(SlashCommandEvent event) {
        ICommand command = this.getCommand(event.getName());
        if (this.preprocess(event, command)) command.exec(event);
    }

    private boolean preprocess(SlashCommandEvent event, ICommand command) {
        if (command == null) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noCommandFound);
            return false;
        }

        if (command.customDefined()) return true;

        String ownerID = ConfigCollection.getConfig().ownerID();

        if (command.isOwnerCommand()) {
            if (!event.getUser().getId().equals(ownerID)) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.ownerRequired);
                return false;
            }
        }

        if (!event.getMember().hasPermission(command.getUserPermissions())) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noPerms);
            return false;
        }

        if (!event.getGuild().getSelfMember().hasPermission(command.getBotPermission())) {
            EmbedUtils.sendEmbed(event, Color.RED, Constants.noBotPerms);
            return false;
        }

        GuildVoiceState botState = event.getGuild().getSelfMember().getVoiceState();
        GuildVoiceState userState = event.getMember().getVoiceState();

        if (command.needConnectedVoice()) {
            if (botState == null || userState == null) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.cannotDetectState);
                return false;
            }
            if (!userState.inVoiceChannel()) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.cannotDetectState);
                return false;
            }
        }

        if (command.sameVoice()) {
            if (botState == null || userState == null) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.cannotDetectState);
                return false;
            }
            if (!userState.inVoiceChannel()) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.cannotDetectState);
                return false;
            }
            if (botState.getChannel() != userState.getChannel()) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.noSameVoice);
                return false;
            }
        }

        GuildMusicManager musicMan = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (command.activePlayer()) {
            if (musicMan.audioPlayer.getPlayingTrack() == null) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.noActivePlayer);
                return false;
            }
        }

        if (command.activeQueue()) {
            if (musicMan.scheduler.queue.isEmpty()) {
                EmbedUtils.sendEmbed(event, Color.RED, Constants.noActiveQueue);
                return false;
            }
        }

        return true;
    }
}
