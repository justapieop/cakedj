package me.justapie.cakedj;

import me.justapie.cakedj.database.MongoDBHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private static final CommandManager COMMAND_MANAGER = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        for (Guild guild : event.getJDA().getGuilds())
            guild.updateCommands().addCommands(COMMAND_MANAGER.dataList).queue();
        LOGGER.info(event.getJDA().getSelfUser().getAsTag() + " is ready");
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        super.onShutdown(event);
        MongoDBHandler.getClient().close();
        LOGGER.warn("Client disconnected on " + event.getTimeShutdown());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        super.onSlashCommand(event);
        COMMAND_MANAGER.process(event);
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
        if (event.getMember().equals(event.getGuild().getSelfMember())) {
            if (event.getChannelJoined() instanceof StageChannel) {
                StageChannel stage = (StageChannel) event.getChannelJoined();
                if (stage.getStageInstance() == null)
                    stage.createStageInstance("Listening Music with CakeDJ").queue();
                event.getGuild().requestToSpeak();
                return;
            }
            AudioManager audioMan = event.getGuild().getAudioManager();
            audioMan.setSelfDeafened(true);
            audioMan.setSelfMuted(false);
        }
    }
}
