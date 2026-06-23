package uk.firedev.migrainebot;

import uk.firedev.migrainebot.discord.MigraineBot;
import uk.firedev.migrainebot.util.Config;

import java.util.logging.Logger;

public class Main {

    public static final boolean TESTING = false;

    public static final long SERVER_ID = TESTING ? 1477810954686300303L : 1493002700067962912L;
    public static final long AUTOMOD_RULE_ID = TESTING ? 1518797860353605642L : 1496606212492497097L;
    public static final long LOG_CHANNEL_ID = TESTING ? 1496613182557520084L : 1493912068095479808L;

    public static final String INDIGENA_WEBHOOK = TESTING
        ? Config.read("testing_indigena_webhook")
        : Config.read("indigena_webhook");

    public static final String FEUD_WEBHOOK = TESTING
        ? Config.read("testing_feud_webhook")
        : Config.read("feud_webhook");

    static void main(String[] args) {
        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}