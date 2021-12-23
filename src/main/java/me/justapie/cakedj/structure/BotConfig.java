package me.justapie.cakedj.structure;

import java.util.List;

public interface BotConfig {
    String getToken();
    List<String> getOwnerIds();
    String getDatabaseUrl();
    String geniusKey();
}
