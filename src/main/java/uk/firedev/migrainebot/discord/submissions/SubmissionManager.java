package uk.firedev.migrainebot.discord.submissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.checkbox.Checkbox;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.unions.IThreadContainerUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Main;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class SubmissionManager extends ListenerAdapter {

    private static final Color SUGGEST_COLOR = new Color(52, 152, 219);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("submit")) {
            return;
        }
        Guild guild = event.getGuild();
        if (guild == null || guild.getIdLong() != Main.SERVER_ID) {
            event.getInteraction().reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("username").getAsString();
        String description = event.getOption("description").getAsString();
        Message.Attachment attachment = event.getOption("submission").getAsAttachment();
        sendEmbed(name, description, attachment, event.getInteraction());
    }

    public void sendEmbed(@NotNull String ign, @NotNull String description, @NotNull Message.Attachment attachment, @NotNull SlashCommandInteraction interaction) {
        MessageEmbed embed = new EmbedBuilder()
            .setColor(SUGGEST_COLOR)
            .setTitle("Submission")
            .addField("In-game Username:", ign, false)
            .addField("Description:", description, false)
            .setImage(attachment.getUrl())
            .build();

        TextChannel channel = MigraineBot.get().getBot().getTextChannelById(Main.SUBMISSION_CHANNEL);
        if (channel == null) {
            interaction.reply("An error has occurred. Please try again later.").setEphemeral(true).queue();
            return;
        }

        channel.sendMessageEmbeds(embed).queue();
        interaction.reply("Your submission has been noted.").setEphemeral(true).queue();

        /*
        suggestionChannel.sendMessageEmbeds(embed)
            .flatMap(msg -> msg.createThreadChannel(title))
            .onSuccess(channel -> {
                // Do not add to thread if anonymous.
                if (!anonymous) {
                    channel.addThreadMember(event.getUser()).queue();
                }
                CommunityServer.get().getSupportRoles().forEach(role ->
                    channel.sendMessage(role.getAsMention()).queue()
                );
            })
            .queue();
        event.getInteraction().reply("Your Suggestion has been noted!").setEphemeral(true).queue();
         */
    }

}
