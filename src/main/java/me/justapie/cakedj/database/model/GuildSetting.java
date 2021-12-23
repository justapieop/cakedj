package me.justapie.cakedj.database.model;

import java.util.List;

public final class GuildSetting {
    private String guildId;
    private String guildName;
    private boolean is247 = false;
    private boolean channelRestrict = false;
    private List<String> djOnlyChannels = List.of();
    private List<String> djRoles = List.of();

    public String getGuildId() {
        return guildId;
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

    public List<String> getDjOnlyChannels() {
        return djOnlyChannels;
    }

    public List<String> getDjRoles() {
        return djRoles;
    }

    public GuildSetting setGuildId(String guildId) {
        this.guildId = guildId;
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
}
