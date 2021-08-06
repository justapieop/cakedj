package me.justapie.cakedj.database.models;

import java.util.List;

public interface UserModel {
    String userID();

    List<List<AudioTrackMin>> playlists();
}

