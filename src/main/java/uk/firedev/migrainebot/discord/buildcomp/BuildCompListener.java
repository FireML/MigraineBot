package uk.firedev.migrainebot.discord.buildcomp;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Main;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.awt.*;
import java.util.Optional;

public class BuildCompListener extends ListenerAdapter {

    private static final Color EMBED_COLOR = new Color(108, 59, 170);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("buildcomp")) {
            return;
        }
        Guild guild = event.getGuild();
        if (guild == null || guild.getIdLong() != Main.SERVER_ID) {
            event.getInteraction().reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("username").getAsString();
        Message.Attachment attachment = event.getOption("submission").getAsAttachment();

        String description = Optional.ofNullable(event.getOption("description"))
            .map(OptionMapping::getAsString)
            .orElse("Not Provided.");

        sendEmbed(name, description, attachment, event.getInteraction());
    }

    public void sendEmbed(@NotNull String ign, @NotNull String description, @NotNull Message.Attachment attachment, @NotNull SlashCommandInteraction interaction) {
        MessageEmbed embed = new EmbedBuilder()
            .setColor(EMBED_COLOR)
            .setTitle("Submission")
            .addField("In-game Username:", ign, false)
            .addField("Description:", description, false)
            .setImage(attachment.getUrl())
            .build();

        MigraineBot.get().buildCompWebhook.sendMessageEmbeds(embed)
            .and(interaction.reply("Your submission has been noted.").setEphemeral(true))
            .queue();
    }

}
