package uk.firedev.migrainebot.discord.buildcomp;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public class BuildCompCommand {

    public static SlashCommandData get() {
        return Commands.slash("buildcomp", "Submit your build").addOptions(getOptions());
    }

    private static List<OptionData> getOptions() {
        return List.of(
            new OptionData(OptionType.STRING, "username", "Username").setRequired(true),
            new OptionData(OptionType.ATTACHMENT, "submission", "Submission").setRequired(true),
            new OptionData(OptionType.STRING, "description", "Description").setRequired(false)
        );
    }

}
