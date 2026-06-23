package uk.firedev.migrainebot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import uk.firedev.migrainebot.Main;
import uk.firedev.migrainebot.discord.indigena.IndigenaListener;
import uk.firedev.migrainebot.discord.indigena.SubmitCommand;
import uk.firedev.migrainebot.util.Config;

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
            Config.getBotToken(),
            EnumSet.allOf(GatewayIntent.class)
        ).setMemberCachePolicy(MemberCachePolicy.ALL);
    }

    public void load() {
        this.bot = buildBot(initializeBuilder());
        awaitBotReady();
        this.bot.addEventListener(new IndigenaListener());
        this.bot.updateCommands().addCommands(
            SubmitCommand.get()
        ).queue();

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

}
