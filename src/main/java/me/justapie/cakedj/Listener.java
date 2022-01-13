package me.justapie.cakedj;

import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.database.MongoDBHandler;
import me.justapie.cakedj.utils.DatabaseUtils;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.bson.Document;
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
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);
        DatabaseUtils.createDocument("guilds", new Document()
                .append("guildID", event.getGuild().getId())
                .append("guildName", event.getGuild().getName())
        );
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        super.onGuildLeave(event);
        DatabaseUtils.deleteDocument("guilds", "guildID", event.getGuild().getId());
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        AudioChannel old = event.getChannelLeft();
        if (!old.getMembers().contains(event.getGuild().getSelfMember())) return;
        long numOfMembers = old.getMembers().stream().filter(
                (c) -> !c.getUser().isBot()
        ).count();
        if (numOfMembers != 0) return;
        Document doc = DatabaseUtils.getDocument("guilds", "guildID", event.getGuild().getId());
        AudioManager audioMan = event.getGuild().getAudioManager();
        GuildMusicManager musicMan = PlayerManager.getInstance().getMusicManager(event.getGuild());
        if (doc.getBoolean("is247")) {
            if (!musicMan.scheduler.getQueueData().getQueue().isEmpty()) return;
            audioMan.closeAudioConnection();
            return;
        }
        audioMan.closeAudioConnection();
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
