package uk.firedev.migrainebot.command.message;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import uk.firedev.migrainebot.Checks;
import uk.firedev.migrainebot.common.Command;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

public class AddMessageCommand extends Command {

    @Override
    public String name() {
        return "addmessagecommand";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name(), "Add a new message command.")
            .addOption(OptionType.STRING, "name", "The name of the command", true)
            .addOption(OptionType.STRING, "message", "The message to send", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!Checks.canUseAdminCommands(event.getMember())) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("name").getAsString();
        String message = event.getOption("message").getAsString();
        Configuration.get().addMsgCommand(name, message);
        event.getInteraction().reply("Your message command has been added. Reloading the bot.").setEphemeral(true).queue();
        MigraineBot.get().reload();
    }

}
