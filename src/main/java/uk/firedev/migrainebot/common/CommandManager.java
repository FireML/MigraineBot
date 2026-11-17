package uk.firedev.migrainebot.discord.msgcommands;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.command.ReloadCommand;
import uk.firedev.migrainebot.command.SettingsCommand;
import uk.firedev.migrainebot.command.message.MessageCommand;
import uk.firedev.migrainebot.common.Command;
import uk.firedev.migrainebot.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private static final CommandManager INSTANCE = new CommandManager();

    private final List<Command> loaded = new ArrayList<>();

    private CommandManager() {}

    public static @NotNull CommandManager get() {
        return INSTANCE;
    }

    public void reload() {
        loaded.clear();

        // Hard Coded
        loaded.add(new ReloadCommand());
        loaded.add(new SettingsCommand());

        // Configurable
        loadMessageCommands();
    }

    public List<? extends Command> getLoaded() {
        return loaded;
    }

    public List<SlashCommandData> buildCommands() {
        return loaded.stream().map(Command::create).toList();
    }

    // Message Commands

    private void loadMessageCommands() {
        Section section = Configuration.get().getConfig().getSection("message-commands");
        if (section == null) {
            return;
        }
        section.getRoutesAsStrings(false).forEach(key -> {
            String val = section.getString(key);
            if (val != null) {
                MessageCommand command = new MessageCommand(key, val);
                loaded.add(command);
            }
        });
    }

    // Event

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String name = event.getName();
        for (Command command : loaded) {
            if (command.name().equals(name)) {
                command.execute(event);
                return;
            }
        }
        event.getInteraction().reply("Invalid command.").setEphemeral(true).queue();
    }

}
