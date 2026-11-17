package uk.firedev.migrainebot.command.message;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import uk.firedev.migrainebot.Checks;
import uk.firedev.migrainebot.common.Command;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;
import uk.firedev.migrainebot.discord.msgcommands.CommandManager;

import java.util.List;
import java.util.Objects;

public class RemoveCommand extends Command {

    @Override
    public String name() {
        return "removemessagecommand";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name(), "Removes a message command.").addOptions(data());
    }

    private OptionData data() {
        List<net.dv8tion.jda.api.interactions.commands.Command.Choice> choices = CommandManager.get().getLoaded().stream()
            .map(c -> (c instanceof MessageCommand msg) ? msg : null)
            .filter(Objects::nonNull)
            .map(s -> new net.dv8tion.jda.api.interactions.commands.Command.Choice(s.name(), s.name()))
            .toList();
        return new OptionData(OptionType.STRING, "name", "The name of the command", true).addChoices(choices);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!Checks.canUseAdminCommands(event.getMember())) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("name").getAsString();
        Configuration.get().removeMsgCommand(name);
        event.getInteraction().reply("Your message command has been removed. Reloading the bot.").setEphemeral(true).queue();
        MigraineBot.get().reload();
    }

}
