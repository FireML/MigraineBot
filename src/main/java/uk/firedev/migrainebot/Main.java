package uk.firedev.migrainebot;

import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    static void main() {
        Configuration.get().reload();

        try {
            Storage.initS3();
        } catch (NullPointerException exception) {
            getLogger().log(Level.SEVERE, "Failed to initialize S3.", exception);
            System.exit(1);
        }

        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}