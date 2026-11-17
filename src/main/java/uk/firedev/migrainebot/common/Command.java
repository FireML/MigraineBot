package uk.firedev.migrainebot.common;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public abstract class Command {

    public abstract String name();

    public abstract SlashCommandData create();

    public abstract void execute(SlashCommandInteractionEvent event);

}
