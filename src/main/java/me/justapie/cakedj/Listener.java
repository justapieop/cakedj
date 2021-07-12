package me.justapie.cakedj;

import me.justapie.cakedj.command.Manager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Listener.class);
    private static final Manager man = new Manager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);

        for (Guild guild : event.getJDA().getGuilds())
            guild.updateCommands().addCommands(man.commandData).queue();

        log.info("Logged in as " + event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        super.onSlashCommand(event);
        man.process(event);
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
        if (event.getChannelJoined() instanceof StageChannel) {
            StageChannel stage = (StageChannel) event.getChannelJoined();
            if (stage.getStageInstance() == null) stage.createStageInstance(Constants.musicWithCakeDJ).queue();
            event.getGuild().requestToSpeak();
            return;
        }
        AudioManager audioMan = event.getGuild().getAudioManager();
        audioMan.setSelfMuted(false);
        audioMan.setSelfDeafened(true);
    }
}
