package uk.firedev.migrainebot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import uk.firedev.migrainebot.common.Command;
import uk.firedev.migrainebot.discord.MigraineBot;

public class ReloadCommand extends Command {

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name(), "Reloads MigraineBot.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.getInteraction().reply("Beep Boop. Reloading.").setEphemeral(true).queue();
        MigraineBot.get().reload();
    }

}
