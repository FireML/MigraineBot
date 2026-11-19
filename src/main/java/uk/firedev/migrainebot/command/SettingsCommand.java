package uk.firedev.migrainebot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import uk.firedev.migrainebot.Checks;
import uk.firedev.migrainebot.config.Configuration;

import java.util.List;

public class SettingsCommand extends uk.firedev.migrainebot.common.Command {

    @Override
    public String name() {
        return "settings";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name(), "Configure MigraineBot.").addOptions(options());
    }

    private List<OptionData> options() {
        return List.of(
            new OptionData(OptionType.STRING, "setting", "The setting to change.").addChoices(choices()).setRequired(true),
            new OptionData(OptionType.STRING, "value", "The new value of the setting.").setRequired(true)
        );
    }

    private List<Command.Choice> choices() {
        return List.of(
            new Command.Choice("AutoMod Rule ID", "automod-rule-id"),
            new Command.Choice("Log Channel ID", "log-channel-id")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();
        if (guild == null || member == null || guild.getIdLong() != Configuration.get().getServerId()) {
            event.getInteraction().reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        if (!Checks.canUseAdminCommands(member)) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }

        String setting = event.getOption("setting").getAsString();
        String value = event.getOption("value").getAsString();

        switch (setting) {
            case "automod-rule-id" -> {
                try {
                    long id = Long.parseLong(value);
                    TextChannel channel = guild.getTextChannelById(id);
                    if (channel == null) {
                        event.getInteraction().reply("That channel doesn't exist in this server?").setEphemeral(true).queue();
                        return;
                    }
                    Configuration.get().setAutoModRuleId(id);
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
                        event.getInteraction().reply("That channel doesn't exist in this server?").setEphemeral(true).queue();
                        return;
                    }
                    Configuration.get().setLogChannelId(id);
                    event.getInteraction().reply("Log Channel ID has been set to " + value).setEphemeral(true).queue();
                } catch (NumberFormatException exception) {
                    event.getInteraction().reply(value + " is not a valid ID").setEphemeral(true).queue();
                }
            }
        }
    }
}
