package uk.firedev.migrainebot.discord.msgcommands;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import uk.firedev.migrainebot.Main;
import uk.firedev.migrainebot.config.Configuration;

import java.util.HashMap;
import java.util.Map;

public class MsgCommandManager {

    private static final MsgCommandManager INSTANCE = new MsgCommandManager();

    private final Map<String, String> loaded = new HashMap<>();

    private MsgCommandManager() {}

    public static @NotNull MsgCommandManager get() {
        return INSTANCE;
    }

    public void reload() {
        loaded.clear();
        Section section = Configuration.get().getConfig().getSection("message-commands");
        if (section == null) {
            return;
        }
        section.getRoutesAsStrings(false).forEach(key -> {
            String val = section.getString(key);
            if (val != null) {
                loaded.put(key, val);
            }
        });
    }

    public Map<String, String> getLoaded() {
        return loaded;
    }

}
