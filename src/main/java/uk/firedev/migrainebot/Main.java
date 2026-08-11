package uk.firedev.migrainebot;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    public static Configuration CONFIG;

    static {
        loadConfig();
        // Save new keys.
        CONFIG.save();
    }

    public static void loadConfig() {
        Path configFile = Paths.get("config.yml");
        CONFIG = ConfigurationLoader.from(configFile)
            .withComments()
            .load(Configuration::new);
    }

    static void main() {
        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}