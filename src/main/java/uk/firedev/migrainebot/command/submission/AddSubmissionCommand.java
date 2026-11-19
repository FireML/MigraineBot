package uk.firedev.migrainebot.command.submission;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Checks;
import uk.firedev.migrainebot.common.Command;
import uk.firedev.migrainebot.config.Configuration;
import uk.firedev.migrainebot.discord.MigraineBot;

public class AddSubmissionCommand extends Command {

    @Override
    public String name() {
        return "addsubmissioncommand";
    }

    @Override
    public SlashCommandData create() {
        return Commands.slash(name(), "Add a new submission command.")
            .addOption(OptionType.STRING, "name", "The name of the command", true)
            .addOption(OptionType.STRING, "channel", "The channel to send to", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null || guild.getIdLong() != Configuration.get().getServerId()) {
            event.getInteraction().reply("You cannot use this command here.").setEphemeral(true).queue();
            return;
        }
        if (!Checks.canUseAdminCommands(event.getMember())) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("name").getAsString();
        GuildMessageChannel channel = getChannel(guild, event.getOption("channel").getAsString());
        if (channel == null) {
            event.getInteraction().reply("Channel must be a message channel.").setEphemeral(true).queue();
            return;
        }
        Configuration.get().addSubmissionCommand(name, channel.getIdLong());
        event.getInteraction().reply("Your submission command has been added. Reloading the bot.").setEphemeral(true).queue();
        MigraineBot.get().reload();
    }

    private GuildMessageChannel getChannel(@NotNull Guild guild, @NotNull String idStr) {
        try {
            long id = Long.parseLong(idStr);
            return guild.getChannelById(GuildMessageChannel.class, id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
