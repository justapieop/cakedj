package me.justapie.cakedj.database.models;

import java.util.Map;

public interface ConfigModel {
    String token();

    String ownerID();

    String dblToken();

    Map<String, String> nodes();

    String rawNodeData();
}
