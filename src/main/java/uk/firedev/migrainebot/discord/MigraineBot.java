package uk.firedev.migrainebot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.IncomingWebhookClient;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import uk.firedev.migrainebot.Main;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.buildcomp.BuildCompListener;
import uk.firedev.migrainebot.discord.buildcomp.BuildCompCommand;
import uk.firedev.migrainebot.discord.indigena.IndigenaListener;
import uk.firedev.migrainebot.discord.indigena.SubmitCommand;
import uk.firedev.migrainebot.discord.msgcommands.MsgCommandListener;
import uk.firedev.migrainebot.discord.msgcommands.MsgCommandManager;
import uk.firedev.migrainebot.discord.msgcommands.MsgCommands;

import java.util.EnumSet;
import java.util.logging.Level;

public class MigraineBot {

    private static final MigraineBot instance = new MigraineBot();
    private JDA bot;

    public IncomingWebhookClient indigenaWebhook;
    public IncomingWebhookClient buildCompWebhook;

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
        this.bot = buildBot(initializeBuilder());
        awaitBotReady();
        this.bot.addEventListener(
            new IndigenaListener(),
            new BuildCompListener(),
            new SettingsCommand(),
            new MsgCommandListener()
        );
        this.bot.updateCommands().addCommands(
            SubmitCommand.get(),
            BuildCompCommand.get(),
            SettingsCommand.get(),
            SettingsCommand.getReload(),
            MsgCommands.getAdd(),
            MsgCommands.getRemove()
        ).addCommands(
            MsgCommands.getMessageCommands()
        ).queue();

        this.indigenaWebhook = WebhookClient.createClient(this.bot, Configuration.get().getIndigenaWebhook());
        this.buildCompWebhook = WebhookClient.createClient(this.bot, Configuration.get().getBuildCompWebhook());

        // Checks every minute for the time because I'm lazy
        DisablePingTask.INSTANCE = new DisablePingTask();
        DisablePingTask.INSTANCE.start();
    }

    public void reload() {
        Main.getLogger().info("Reloading MigraineBot.");
        unload();
        Configuration.get().reload();
        MsgCommandManager.get().reload();
        load();
    }

    public void unload() {
        this.bot.shutdown();
        this.indigenaWebhook = null;
        this.buildCompWebhook = null;
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
