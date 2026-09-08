package uk.firedev.migrainebot;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.discord.MigraineBot;

public class SubmissionManager {

    private static final SubmissionManager INSTANCE = new SubmissionManager();

    private SubmissionManager() {}

    public static @NotNull SubmissionManager get() {
        return INSTANCE;
    }

    public void submit(long threadId, @NotNull SlashCommandInteraction interaction, @NotNull MessageEmbed embed) {
        ThreadChannel thread = MigraineBot.get().getBot().getThreadChannelById(threadId);
        if (thread == null) {
            interaction.reply("Could not process your submission. Please ping <@767886112177389599> or try again later.").queue();
            return;
        }
        thread.sendMessageEmbeds(embed).queue();
        interaction.reply("Your submission has been noted.").setEphemeral(true).queue();
    }

}
