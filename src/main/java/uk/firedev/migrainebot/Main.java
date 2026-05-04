package uk.firedev.migrainebot;

import uk.firedev.migrainebot.discord.MigraineBot;

import java.util.logging.Logger;

public class Main {

    public static final boolean TESTING = false;

    public static final long SERVER_ID = TESTING ? 1477810954686300303L : 1493002700067962912L;
    public static final long AUTOMOD_RULE_ID = TESTING ? 1496607451896877146L : 1496606212492497097L;
    public static final long LOG_CHANNEL_ID = TESTING ? 1496613182557520084L : 1493912068095479808L;
    public static final long SUBMISSION_CHANNEL = TESTING ? 1499432780684333197L : 1499663935262162954L;

    static void main(String[] args) {
        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}