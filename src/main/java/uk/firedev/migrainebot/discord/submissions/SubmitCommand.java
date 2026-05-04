package uk.firedev.migrainebot.discord.submissions;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public class SubmitCommand {

    public static SlashCommandData get() {
        return Commands.slash("submit", "Submit").addOptions(getOptions());
    }

    private static List<OptionData> getOptions() {
        return List.of(
            new OptionData(OptionType.STRING, "username", "Username").setRequired(true),
            new OptionData(OptionType.STRING, "description", "Description").setRequired(true),
            new OptionData(OptionType.ATTACHMENT, "submission", "Submission").setRequired(true)
        );
    }

}
