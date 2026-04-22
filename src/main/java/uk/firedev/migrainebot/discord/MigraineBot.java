package uk.firedev.migrainebot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import uk.firedev.migrainebot.Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.logging.Level;

public class MigraineBot {

    private static final MigraineBot instance = new MigraineBot();
    private JDA bot;

    public static MigraineBot get() {
        return instance;
    }

    private MigraineBot() {}

    protected JDABuilder initializeBuilder() {
        return JDABuilder.createLight(
            getToken(),
            EnumSet.allOf(GatewayIntent.class)
        ).setMemberCachePolicy(MemberCachePolicy.ALL);
    }

    public void load() {
        this.bot = buildBot(initializeBuilder());
        awaitBotReady();

        // Checks every minute for the time because I'm lazy
        DisablePingTask.getInstance().start();
    }

    public JDA getBot() {
        return bot;
    }

    private JDA buildBot(JDABuilder builder) {
        return builder.build();
    }

    private void awaitBotReady() {
        try {
            this.bot.awaitReady();
        } catch (InterruptedException exception) {
            Main.getLogger().log(Level.SEVERE, "Waiting for bot to load was interrupted!", exception);
        }
    }

    private static String getToken() {
        Path path = Paths.get("discord_token");
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            return Files.readString(path, StandardCharsets.UTF_8).trim();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
