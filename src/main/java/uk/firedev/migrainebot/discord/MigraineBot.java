package uk.firedev.migrainebot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import uk.firedev.migrainebot.Main;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.msgcommands.CommandManager;

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
            Configuration.get().getBotToken(),
            EnumSet.allOf(GatewayIntent.class)
        ).setMemberCachePolicy(MemberCachePolicy.ALL);
    }

    public void load() {
        CommandManager commands = CommandManager.get();
        commands.reload();

        this.bot = buildBot(initializeBuilder());
        awaitBotReady();
        this.bot.addEventListener(commands);
        this.bot.updateCommands()
            .addCommands(commands.buildCommands())
            .queue();

        // Checks every minute for the time because I'm lazy
        DisablePingTask.INSTANCE = new DisablePingTask();
        DisablePingTask.INSTANCE.start();
    }

    public void reload() {
        Main.getLogger().info("Reloading MigraineBot.");
        unload();
        Configuration.get().reload();
        load();
    }

    public void unload() {
        this.bot.shutdown();
        DisablePingTask.INSTANCE.cancel();
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
