package uk.firedev.migrainebot.discord.feud;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public class FeudSubmitCommand {

    public static SlashCommandData get() {
        return Commands.slash("feud-submit", "Submit your answer");
    }

}
