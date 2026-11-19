package uk.firedev.migrainebot.command.submission;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Storage;
import uk.firedev.migrainebot.common.Command;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SubmissionCommand extends Command {

    private static final Color EMBED_COLOR = new Color(108, 59, 170);
    private static final List<OptionData> OPTIONS = List.of(
        new OptionData(OptionType.STRING, "username", "Username").setRequired(true),
        new OptionData(OptionType.ATTACHMENT, "submission", "Submission").setRequired(true),
        new OptionData(OptionType.STRING, "description", "Description").setRequired(false)
    );

    private final String name;
    private final long channel;

    public SubmissionCommand(String name, long channel) {
        this.name = name;
        this.channel = channel;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name, name).addOptions(OPTIONS);
    }

    public void execute(SlashCommandInteractionEvent event) {
        if (!event.getName().equals(name)) {
            return;
        }
        Guild guild = event.getGuild();
        if (guild == null || guild.getIdLong() != Configuration.get().getServerId()) {
            event.getInteraction().reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("username").getAsString();
        Message.Attachment attachment = event.getOption("submission").getAsAttachment();
        if (!attachment.isImage()) {
            event.getInteraction().reply("Attachment must be an image.").setEphemeral(true).queue();
            return;
        }
        String description = Optional.ofNullable(event.getOption("description"))
            .map(OptionMapping::getAsString)
            .orElse("Not Provided.");

        submit(event, name, description, attachment);
    }

    public void submit(@NotNull SlashCommandInteraction interaction, @NotNull String ign, @NotNull String description, @NotNull Message.Attachment attachment) {
        GuildMessageChannel channel = MigraineBot.get().getBot().getChannelById(GuildMessageChannel.class, this.channel);
        if (channel == null) {
            interaction.reply("Could not process your submission. Please notify staff or try again later.").queue();
            return;
        }
        interaction.deferReply(true).queue(callback -> {
            String attachmentUrl = Storage.saveAttachment(attachment, callback);
            if (attachmentUrl == null) {
                return;
            }
            MessageEmbed embed = new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle("Submission (/" + name + ")")
                .addField("In-game Username:", ign, false)
                .addField("Description:", description, false)
                .setImage(attachmentUrl)
                .build();
            channel.sendMessageEmbeds(embed).queue();
            callback.sendMessage("Your submission has been noted.").setEphemeral(true).queue();
        });
    }

}
