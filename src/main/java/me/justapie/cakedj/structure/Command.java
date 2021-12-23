package me.justapie.cakedj.structure;

import me.justapie.cakedj.Context;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public abstract class Command {
    public CommandData data;
    public List<Permission> userPerms = List.of();
    public List<Permission> botPerms = List.of();
    public boolean voiceCon = false;
    public boolean sameVoice = false;

    public abstract void execute(Context context);
}
