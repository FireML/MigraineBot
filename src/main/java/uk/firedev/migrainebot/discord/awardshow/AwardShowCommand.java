package uk.firedev.migrainebot.discord.awardshow;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AwardShowCommand extends ListenerAdapter {

    public static SlashCommandData get() {
        return Commands.slash("awardshow", "Sends a countdown for the award show");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("awardshow")) {
            return;
        }
        event.getInteraction().reply("The award show starts: <t:1797552000:R>").setEphemeral(true).queue();
    }

}
