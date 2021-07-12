package me.justapie.cakedj.database.models;

import java.util.List;

public interface GuildModel {
    String guildName();
    String guildID();

    default boolean is247() {
        return false;
    }

    default boolean channelRestrict() {
        return false;
    }

    default List<String> djOnlyChannels() {
        return List.of();
    }
}
