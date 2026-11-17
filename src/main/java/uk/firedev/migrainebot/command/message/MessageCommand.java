package uk.firedev.migrainebot.command.message;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import uk.firedev.migrainebot.common.Command;

public class MessageCommand extends Command {

    private final String name;
    private final String message;

    public MessageCommand(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name, name);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.getInteraction().reply(message).setEphemeral(true).queue();
    }

}
