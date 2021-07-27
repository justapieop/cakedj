package me.justapie.cakedj;

import lavalink.client.io.jda.JdaLavalink;
import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.Manager;
import me.justapie.cakedj.database.collections.ConfigCollection;
import me.justapie.cakedj.database.collections.GuildCollection;
import me.justapie.cakedj.database.models.GuildModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.discordbots.api.client.DiscordBotListAPI;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class Listener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(Listener.class);
    private static final Manager man = new Manager();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);

        if (!ConfigCollection.getConfig().rawNodeData().isEmpty()) {
            JdaLavalink lavalink = new JdaLavalink(
                    event.getJDA().getSelfUser().getId(),
                    event.getJDA().getShardManager().getShardsTotal(),
                    shardId -> event.getJDA().getShardManager().getShardById(shardId)
            );
            ConfigCollection.getConfig().nodes().forEach((key, val) -> {
                try {
                    lavalink.addNode(new URI(key), val);
                } catch (URISyntaxException ignored) {
                }
            });
        }

        if (ConfigCollection.getConfig().dblToken() != null || !ConfigCollection.getConfig().dblToken().isEmpty()) {
            DiscordBotListAPI botListAPI = new DiscordBotListAPI.
                    Builder()
                    .botId(event.getJDA().getSelfUser().getId())
                    .token(ConfigCollection.getConfig().dblToken())
                    .build();

            new Timer().scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            botListAPI.setStats(event.getGuildTotalCount());
                        }
                    }
                    , 0, 3600000
            );
        }

        for (Guild guild : event.getJDA().getGuilds())
            guild.updateCommands().addCommands(man.commandData).queue();

        log.info("Logged in as " + event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        super.onSlashCommand(event);
        if (event.getGuild() == null) return;
        GuildModel guildSetting = GuildCollection.getGuildConfig(event.getGuild());
        if (guildSetting == null) return;
        if (guildSetting.channelRestrict())
            if (!guildSetting.djOnlyChannels().contains(event.getChannel().getId()))
                return;
        man.process(event);
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
        if (event.getChannelJoined().getMembers().contains(event.getGuild().getSelfMember())) {
            if (event.getChannelJoined() instanceof StageChannel stage) {
                if (stage.getStageInstance() == null)
                    stage.createStageInstance(Constants.musicWithCakeDJ).queue();
                event.getGuild().requestToSpeak();
                return;
            }
            AudioManager audioMan = event.getGuild().getAudioManager();
            audioMan.setSelfMuted(false);
            audioMan.setSelfDeafened(true);
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        GuildModel guildSetting = GuildCollection.getGuildConfig(event.getGuild());
        if (guildSetting == null) return;
        if (guildSetting.is247()) return;
        GuildMusicManager musicMan = PlayerManager.getInstance().getMusicManager(event.getGuild());
        if (event.getMember().equals(event.getGuild().getSelfMember())) {
            musicMan.audioPlayer.setFilterFactory(null);
            GuildCollection.modifyGuildConfig(event.getGuild(), Constants.is247Key, false);
            musicMan.audioPlayer.destroy();
        } else {
            if (event.getChannelLeft().getMembers().contains(event.getGuild().getSelfMember())) {
                List<Member> members = event.getChannelLeft().getMembers();
                members = members.stream().filter(
                        (member) -> !member.getUser().isBot()
                ).collect(Collectors.toList());
                if (members.isEmpty()) {
                    musicMan.audioPlayer.destroy();
                    if (event.getChannelLeft() instanceof StageChannel stage) {
                        if (stage.getStageInstance() != null)
                            stage.getStageInstance().delete().queue();
                    }

                    event.getGuild().getAudioManager().closeAudioConnection();
                }
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
        if (event.getMessage().getMentionedMembers().contains(
                event.getGuild().getSelfMember()
        )) {
            event.getChannel().sendMessageEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.GREEN)
                            .setDescription(Constants.announcement)
                            .build()
            ).queue();
        }
    }

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent event) {
        super.onTextChannelDelete(event);
        GuildModel guildSetting = GuildCollection.getGuildConfig(event.getGuild());
        assert guildSetting != null;
        List<String> djOnlyChannels = guildSetting.djOnlyChannels();
        djOnlyChannels.remove(event.getChannel().getId());
        GuildCollection.modifyGuildConfig(event.getGuild(), Constants.djOnlyChannelsKey, djOnlyChannels);
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        super.onGuildLeave(event);
        GuildCollection.deleteGuildConfig(event.getGuild());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);
        event.getGuild().updateCommands().addCommands(man.commandData).queue();
        GuildCollection.createGuildConfig(event.getGuild());
    }
}
