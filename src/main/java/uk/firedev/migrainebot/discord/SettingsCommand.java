package uk.firedev.migrainebot.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Main;

import java.util.List;

public class SettingsCommand extends ListenerAdapter {

    public static SlashCommandData get() {
        return Commands.slash("settings", "Configure MigraineBot.").addOptions(getOptions());
    }

    public static SlashCommandData getReload() {
        return Commands.slash("reload", "Reloads MigraineBot.");
    }

    private static List<OptionData> getOptions() {
        return List.of(
            new OptionData(OptionType.STRING, "setting", "The setting to change.").addChoices(getSettingChoices()).setRequired(true),
            new OptionData(OptionType.STRING, "value", "The new value of the setting.").setRequired(true)
        );
    }

    private static List<Command.Choice> getSettingChoices() {
        return List.of(
            new Command.Choice("AutoMod Rule ID", "automod-rule-id"),
            new Command.Choice("Log Channel ID", "log-channel-id"),
            new Command.Choice("Indigena Webhook", "indigena-webhook"),
            new Command.Choice("Build Comp Webhook", "build-comp-webhook")
        );
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("settings") && !event.getName().equals("reload")) {
            return;
        }
        Guild guild = event.getGuild();
        Member member = event.getMember();
        if (guild == null || member == null || guild.getIdLong() != Main.CONFIG.serverId) {
            event.getInteraction().reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        // Allows configured role and me (FireML) to access commands.
        boolean canUse = member.getIdLong() == 767886112177389599L || member.getRoles().stream().anyMatch(role -> role.getIdLong() == Main.CONFIG.settingsRoleId);
        if (!canUse) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }
        switch (event.getName()) {
            case "settings" -> settings(event, guild);
            case "reload" -> reload(event);
        }
    }

    private void reload(@NotNull SlashCommandInteractionEvent event) {
        event.getInteraction().reply("Beep Boop. Reloading.").queue();
        MigraineBot.get().reload();
    }

    private void settings(@NotNull SlashCommandInteractionEvent event, @NotNull Guild guild) {
        String setting = event.getOption("setting").getAsString();
        String value = event.getOption("value").getAsString();

        switch (setting) {
            case "automod-rule-id" -> {
                try {
                    long id = Long.parseLong(value);
                    TextChannel channel = guild.getTextChannelById(id);
                    if (channel == null) {
                        event.getInteraction().reply("That channel doesn't exist in this server?").queue();
                        return;
                    }
                    Main.CONFIG.autoModRuleId = Long.parseLong(value);
                    Main.CONFIG.save();
                    event.getInteraction().reply("AutoMod Rule ID has been set to " + value).setEphemeral(true).queue();
                } catch (NumberFormatException exception) {
                    event.getInteraction().reply(value + " is not a valid ID").setEphemeral(true).queue();
                }
            }
            case "log-channel-id" -> {
                try {
                    long id = Long.parseLong(value);
                    TextChannel channel = guild.getTextChannelById(id);
                    if (channel == null) {
                        event.getInteraction().reply("That channel doesn't exist in this server?").queue();
                        return;
                    }
                    Main.CONFIG.logChannelId = id;
                    Main.CONFIG.save();
                    event.getInteraction().reply("Log Channel ID has been set to " + value).setEphemeral(true).queue();
                } catch (NumberFormatException exception) {
                    event.getInteraction().reply(value + " is not a valid ID").setEphemeral(true).queue();
                }
            }
            case "indigena-webhook" -> {
                try {
                    MigraineBot.get().indigenaWebhook = WebhookClient.createClient(MigraineBot.get().getBot(), value);
                    Main.CONFIG.webhooks.indigena = value;
                    Main.CONFIG.save();
                    event.getInteraction().reply("Successfully changed Indigena Webhook").setEphemeral(true).queue();
                } catch (IllegalArgumentException exception) {
                    event.getInteraction().reply("Invalid Indigena Webhook").setEphemeral(true).queue();
                }
            }
            case "build-comp-webhook" -> {
                try {
                    MigraineBot.get().buildCompWebhook = WebhookClient.createClient(MigraineBot.get().getBot(), value);
                    Main.CONFIG.webhooks.buildComp = value;
                    Main.CONFIG.save();
                    event.getInteraction().reply("Successfully changed Build Comp Webhook").setEphemeral(true).queue();
                } catch (IllegalArgumentException exception) {
                    event.getInteraction().reply("Invalid Build Comp Webhook").setEphemeral(true).queue();
                }
            }
        }
    }

}
