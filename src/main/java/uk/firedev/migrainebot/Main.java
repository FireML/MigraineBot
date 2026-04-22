package uk.firedev.migrainebot;

import uk.firedev.migrainebot.discord.MigraineBot;

import java.util.logging.Logger;

public class Main {

    static void main(String[] args) {
        MigraineBot.get().load();
        Main.getLogger().info("Loaded!");
    }

    public static Logger getLogger() {
        return Logger.getLogger("MigraineBot");
    }

}