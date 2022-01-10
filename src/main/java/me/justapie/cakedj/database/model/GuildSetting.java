package me.justapie.cakedj.database.model;

import me.justapie.cakedj.database.DatabaseUtils;
import org.bson.Document;

import java.util.List;

public final class GuildSetting {
    private String guildID;
    private String guildName;
    private boolean is247 = false;
    private boolean channelRestrict = false;
    private boolean djRoleMode = false;
    private List<String> djOnlyChannels = List.of();
    private List<String> djRoles = List.of();

    public String getGuildID() {
        return guildID;
    }

    public String getGuildName() {
        return guildName;
    }

    public boolean is247() {
        return is247;
    }

    public boolean channelRestrict() {
        return channelRestrict;
    }

    public boolean djRoleMode() {
        return djRoleMode;
    }

    public List<String> getDjOnlyChannels() {
        return djOnlyChannels;
    }

    public List<String> getDjRoles() {
        return djRoles;
    }

    public GuildSetting setGuildId(String guildID) {
        this.guildID = guildID;
        return this;
    }

    public GuildSetting setGuildName(String guildName) {
        this.guildName = guildName;
        return this;
    }

    public GuildSetting setIs247(boolean is247) {
        this.is247 = is247;
        return this;
    }

    public GuildSetting setDjRoleMode(boolean djRoleMode) {
        this.djRoleMode = djRoleMode;
        return this;
    }

    public GuildSetting setChannelRestrict(boolean channelRestrict) {
        this.channelRestrict = channelRestrict;
        return this;
    }

    public GuildSetting setDjOnlyChannels(List<String> djOnlyChannels) {
        this.djOnlyChannels = djOnlyChannels;
        return this;
    }

    public GuildSetting setDjRoles(List<String> djRoles) {
        this.djRoles = djRoles;
        return this;
    }

    public void update() {
        Document document = new Document()
                .append("guildID", this.guildID)
                .append("guildName", this.guildName)
                .append("is247", this.is247)
                .append("channelRestrict", this.channelRestrict)
                .append("djRoleMode", this.djRoleMode)
                .append("djOnlyChannels", this.djOnlyChannels)
                .append("djRoles", this.djRoles);
        DatabaseUtils.updateData(this.guildID, document);
    }
}
