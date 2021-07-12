package me.justapie.cakedj;

public class Constants {
    public final static String noPerms = "You don't have permission to do this";
    public final static String noBotPerms = "I don't have enough permissions to do this";
    public final static String noVoiceConnection = "You must connect to a voice or a stage channel first";
    public final static String noSameVoice = "You must connect to the same voice or a stage channel with the bot first";
    public final static String noActivePlayer = "There aren't any track being played";
    public final static String noActiveQueue = "The queue is empty. Please add a track first";
    public final static String noCommandFound = "No command found";
    public final static String cannotDetectState = "Cannot detect your voice state";

    // Database Related Things
    public final static String databaseName = "CakeDJ";
    public final static String databaseAddressKey = "DATABASE";

    // Collection Things
    public final static String configCollection = "config";
    public final static String noConfigDefined = "No config defined";
    public final static String tokenKey = "token";
    public final static String ownerIDKey = "ownerID";
    public final static String dblTokenKey = "DBLToken";
    public final static String commandDirName = "commands";
    public final static String eventDirName = "events";
}
