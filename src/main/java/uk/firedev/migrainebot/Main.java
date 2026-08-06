package uk.firedev.migrainebot;

import de.bsommerfeld.jshepherd.core.ConfigurationLoader;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Main {

    public static final Configuration CONFIG;

    static {
        Path configFile = Paths.get("config.yml");
        CONFIG = ConfigurationLoader.from(configFile)
            .withComments()
            .load(Configuration::new);
        // Save new keys.
        CONFIG.save();
    }

    static void main(String[] args) {
        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}