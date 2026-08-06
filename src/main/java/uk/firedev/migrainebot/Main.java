package uk.firedev.migrainebot;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    public static final boolean TESTING = true;

    public static final long SERVER_ID = TESTING ? 1477810954686300303L : 1493002700067962912L;
    public static final long AUTOMOD_RULE_ID = TESTING ? 1518797860353605642L : 1496606212492497097L;
    public static final long LOG_CHANNEL_ID = TESTING ? 1496613182557520084L : 1493912068095479808L;

    public static final Configuration CONFIG;

    static {
        Path configFile = Paths.get("config.yml");
        CONFIG = ConfigurationLoader.from(configFile)
            .withComments()
            .load(Configuration::new);
    }

    static void main(String[] args) {
        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}