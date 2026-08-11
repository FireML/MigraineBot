package uk.firedev.migrainebot.discord.msgcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Checks;
import uk.firedev.migrainebot.config.Configuration;

public class MsgCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String name = event.getName();
        switch (name) {
            case "addcommand" -> addCommand(event);
            case "removecommand" -> removeCommand(event);
            default -> {
                String message = MsgCommandManager.get().getLoaded().get(name);
                if (message == null) {
                    return;
                }
                event.getInteraction().reply(message).setEphemeral(true).queue();
            }
        }
    }

    private void addCommand(SlashCommandInteractionEvent event) {
        if (!Checks.canUseAdminCommands(event.getMember())) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("name").getAsString();
        String message = event.getOption("message").getAsString();
        Configuration.get().addMsgCommand(name, message);
        event.getInteraction().reply("Your message command has been added. Reload the bot to update commands.").queue();
    }

    private void removeCommand(SlashCommandInteractionEvent event) {
        if (!Checks.canUseAdminCommands(event.getMember())) {
            event.getInteraction().reply("You are not permitted to use this command.").setEphemeral(true).queue();
            return;
        }
        String name = event.getOption("name").getAsString();
        Configuration.get().removeMsgCommand(name);
        event.getInteraction().reply("Your message command has been removed. Reload the bot to update commands.").queue();
    }

}
