package me.justapie.cakedj.database.models;

import com.sedmelluq.lava.extensions.youtuberotator.tools.ip.IpBlock;

import java.util.List;

public interface ConfigModel {
    String token();

    String ownerID();

    String dblToken();

    List<IpBlock> ipv6Block();
}
