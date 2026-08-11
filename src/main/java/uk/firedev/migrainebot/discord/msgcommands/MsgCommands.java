package uk.firedev.migrainebot.discord.msgcommands;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import uk.firedev.migrainebot.config.Configuration;

import java.util.List;

public class MsgCommands {

    public static SlashCommandData getAdd() {
        return Commands.slash("addcommand", "Add a new message command.")
            .addOption(OptionType.STRING, "name", "The name of the command", true)
            .addOption(OptionType.STRING, "message", "The message to send", true);
    }

    public static SlashCommandData getRemove() {
        return Commands.slash("removecommand", "Removes a message command.").addOptions(getRemoveData());
    }

    private static OptionData getRemoveData() {
        List<Command.Choice> choices = MsgCommandManager.get().getLoaded().keySet().stream()
            .map(s -> new Command.Choice(s, s))
            .toList();
        return new OptionData(OptionType.STRING, "name", "The name of the command", true).addChoices(choices);
    }

    public static SlashCommandData[] getMessageCommands() {
        return MsgCommandManager.get().getLoaded().keySet().stream()
            .map(s -> Commands.slash(s, s))
            .toArray(SlashCommandData[]::new);
    }

}
