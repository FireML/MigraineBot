package uk.firedev.migrainebot.discord.feud;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Main;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.awt.*;

public class FeudListener extends ListenerAdapter {

    private static final Color EMBED_COLOR = new Color(108, 59, 170);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("feud-submit")) {
            return;
        }
        Guild guild = event.getGuild();
        if (guild == null || guild.getIdLong() != Main.SERVER_ID) {
            event.getInteraction().reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        sendModal(event.getInteraction());
    }

    public void sendModal(@NotNull SlashCommandInteraction interaction) {
        TextInput ign = TextInput.create("ign", TextInputStyle.SHORT)
            .setMinLength(1)
            .setRequired(true)
            .build();
        TextInput submission = TextInput.create("submission", TextInputStyle.PARAGRAPH)
            .setMinLength(1)
            .setRequired(true)
            .build();
        Modal modal = Modal.create("feud-submit", "Submit your answer.")
            .addComponents(
                Label.of("In-Game Username", ign),
                Label.of("Submission", submission)
            )
            .build();
        interaction.replyModal(modal).queue();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null || guild.getIdLong() != Main.SERVER_ID) {
            return;
        }
        if (!event.getModalId().equals("feud-submit")) {
            return;
        }
        String ign = event.getValue("ign").getAsString();
        String submission = event.getValue("submission").getAsString();

        MessageEmbed embed = new EmbedBuilder()
            .setColor(EMBED_COLOR)
            .setTitle("New Submission")
            .addField("In-game Username:", ign, false)
            .addField("Submission:", submission, false)
            .build();

        MigraineBot.get().feudWebhook.sendMessage(MessageCreateData.fromEmbeds(embed))
            .and(event.getInteraction().reply("Your submission has been noted.").setEphemeral(true))
            .queue();
    }

}
