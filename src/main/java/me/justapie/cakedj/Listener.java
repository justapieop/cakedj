package me.justapie.cakedj;

import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.command.Manager;
import me.justapie.cakedj.database.collections.GuildCollection;
import me.justapie.cakedj.database.models.GuildModel;
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
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

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
            List<Member> members = event.getChannelLeft().getMembers();
            members = members.stream().filter(
                    (member) -> !member.getUser().isBot()
            ).collect(Collectors.toList());
            if (members.isEmpty()) {
                musicMan.audioPlayer.destroy();
                event.getGuild().getAudioManager().closeAudioConnection();
            }
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
        GuildCollection.createGuildConfig(event.getGuild());
    }
}
