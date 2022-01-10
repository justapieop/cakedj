package me.justapie.cakedj;

import me.justapie.cakedj.audio.GuildMusicManager;
import me.justapie.cakedj.audio.PlayerManager;
import me.justapie.cakedj.database.DatabaseUtils;
import me.justapie.cakedj.database.model.GuildSetting;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ComponentLayout;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public final class Context implements InteractionHook {
    private final InteractionHook hook;
    private final GuildMusicManager musicManager;
    private final GuildSetting guildSetting;
    private final SlashCommandEvent event;

    public Context(InteractionHook hook, SlashCommandEvent event) {
        super();
        this.hook = hook;
        this.musicManager = PlayerManager.getInstance().getMusicManager(this.hook.getInteraction().getGuild());
        Document doc = DatabaseUtils.getDocument("guilds", "guildID", this.hook.getInteraction().getGuild().getId());
        this.guildSetting = new GuildSetting()
                .setGuildId(doc.getString("guildID"))
                .setGuildName(doc.getString("guildName"))
                .setIs247(doc.getBoolean("is247"))
                .setChannelRestrict(doc.getBoolean("channelRestrict"))
                .setDjOnlyChannels(doc.getList("djOnlyChannel", String.class, List.of()))
                .setDjRoles(doc.getList("djRoles", String.class, List.of()));
        this.event = event;
    }

    public List<OptionMapping> getOptions() { return this.event.getOptions(); }

    public GuildMusicManager getMusicManager() {
        return this.musicManager;
    }

    public GuildSetting getGuildSetting() { return this.guildSetting; }

    @Override
    public long getExpirationTimestamp() {
        return this.hook.getExpirationTimestamp();
    }

    @Override
    public boolean isExpired() {
        return this.hook.isExpired();
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginal(@NotNull String content) {
        return this.hook.editOriginal(content);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginalComponents(@NotNull Collection<? extends ComponentLayout> components) {
        return this.hook.editOriginalComponents(components);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginalComponents(@NotNull ComponentLayout @NotNull ... components) {
        return this.hook.editOriginalComponents(components);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginalEmbeds(@NotNull Collection<? extends MessageEmbed> embeds) {
        return this.hook.editOriginalEmbeds(embeds);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginalEmbeds(@NotNull MessageEmbed @NotNull ... embeds) {
        return this.hook.editOriginalEmbeds(embeds);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginal(@NotNull Message message) {
        return this.hook.editOriginal(message);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginalFormat(@NotNull String format, Object @NotNull ... args) {
        return this.hook.editOriginalFormat(format, args);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginal(@NotNull InputStream data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editOriginal(data, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginal(@NotNull File file, @NotNull AttachmentOption @NotNull ... options) {
        return this.hook.editOriginal(file, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginal(@NotNull File file, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editOriginal(file, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editOriginal(byte @NotNull [] data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editOriginal(data, name, options);
    }

    @NotNull
    @Override
    public RestAction<Void> deleteOriginal() {
        return this.hook.deleteOriginal();
    }

    @NotNull
    @Override
    public Interaction getInteraction() {
        return this.hook.getInteraction();
    }

    @NotNull
    @Override
    public InteractionHook setEphemeral(boolean ephemeral) {
        return this.hook.setEphemeral(ephemeral);
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return this.hook.getJDA();
    }

    @NotNull
    @Override
    public RestAction<Message> retrieveOriginal() {
        return this.hook.retrieveOriginal();
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendMessage(@NotNull String content) {
        return this.hook.sendMessage(content);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendMessage(@NotNull Message message) {
        return this.hook.sendMessage(message);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendMessageEmbeds(@NotNull Collection<? extends MessageEmbed> embeds) {
        return this.hook.sendMessageEmbeds(embeds);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendFile(@NotNull InputStream data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.sendFile(data, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(@NotNull String messageId, @NotNull String content) {
        return this.hook.editMessageById(messageId, content);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(@NotNull String messageId, @NotNull Message message) {
        return this.hook.editMessageById(messageId, message);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageEmbedsById(@NotNull String messageId, @NotNull Collection<? extends MessageEmbed> embeds) {
        return this.hook.editMessageEmbedsById(messageId, embeds);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageComponentsById(@NotNull String messageId, @NotNull Collection<? extends ComponentLayout> components) {
        return this.hook.editMessageComponentsById(messageId, components);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(@NotNull String messageId, @NotNull InputStream data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, data, name, options);
    }

    @NotNull
    @Override
    public RestAction<Void> deleteMessageById(@NotNull String messageId) {
        return this.hook.deleteMessageById(messageId);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendMessageFormat(@NotNull String format, Object @NotNull ... args) {
        return this.hook.sendMessageFormat(format, args);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendMessageEmbeds(@NotNull MessageEmbed embed, MessageEmbed @NotNull ... embeds) {
        return this.hook.sendMessageEmbeds(embed, embeds);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendFile(@NotNull File file, AttachmentOption @NotNull ... options) {
        return this.hook.sendFile(file, options);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendFile(@NotNull File file, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.sendFile(file, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageAction<Message> sendFile(byte @NotNull [] data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.sendFile(data, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(long messageId, @NotNull String content) {
        return this.hook.editMessageById(messageId, content);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(long messageId, Message message) {
        return this.hook.editMessageById(messageId, message);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageFormatById(@NotNull String messageId, @NotNull String format, Object @NotNull ... args) {
        return this.hook.editMessageFormatById(messageId, format, args);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageFormatById(long messageId, @NotNull String format, Object @NotNull ... args) {
        return this.hook.editMessageFormatById(messageId, format, args);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageEmbedsById(long messageId, @NotNull Collection<? extends MessageEmbed> embeds) {
        return this.hook.editMessageEmbedsById(messageId, embeds);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageEmbedsById(@NotNull String messageId, MessageEmbed @NotNull ... embeds) {
        return this.hook.editMessageEmbedsById(messageId, embeds);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageEmbedsById(long messageId, MessageEmbed @NotNull ... embeds) {
        return this.hook.editMessageEmbedsById(messageId, embeds);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageComponentsById(long messageId, @NotNull Collection<? extends ComponentLayout> components) {
        return this.hook.editMessageComponentsById(messageId, components);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageComponentsById(@NotNull String messageId, ComponentLayout @NotNull ... components) {
        return this.hook.editMessageComponentsById(messageId, components);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageComponentsById(long messageId, ComponentLayout @NotNull ... components) {
        return this.hook.editMessageComponentsById(messageId, components);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(@NotNull String messageId, @NotNull File file, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, file, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(@NotNull String messageId, @NotNull File file, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, file, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(@NotNull String messageId, byte @NotNull [] data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, data, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(long messageId, @NotNull InputStream data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, data, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(long messageId, @NotNull File file, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, file, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(long messageId, @NotNull File file, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, file, name, options);
    }

    @NotNull
    @Override
    public WebhookMessageUpdateAction<Message> editMessageById(long messageId, byte @NotNull [] data, @NotNull String name, AttachmentOption @NotNull ... options) {
        return this.hook.editMessageById(messageId, data, name, options);
    }

    @NotNull
    @Override
    public RestAction<Void> deleteMessageById(long messageId) {
        return this.hook.deleteMessageById(messageId);
    }
}
