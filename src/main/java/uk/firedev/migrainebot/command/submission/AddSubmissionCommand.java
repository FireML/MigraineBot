package uk.firedev.migrainebot.command.submission;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import uk.firedev.migrainebot.Checks;
import uk.firedev.migrainebot.common.Command;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

public class AddSubmissionCommand extends Command {

    @Override
    public String name() {
        return "addsubmissioncommand";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name(), "Add a new submission command.")
            .addOption(OptionType.STRING, "name", "The name of the command", true)
            .addOption(OptionType.INTEGER, "channel", "The channel to send to", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!Checks.canUseAdminCommands(event.getMember())) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("name").getAsString();
        long channel = event.getOption("channel").getAsLong();
        Configuration.get().addSubmissionCommand(name, channel);
        event.getInteraction().reply("Your submission command has been added. Reloading the bot.").setEphemeral(true).queue();
        MigraineBot.get().reload();
    }

}
