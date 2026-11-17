package uk.firedev.migrainebot.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Configuration {

    private static final Configuration INSTANCE = new Configuration();
    private static final File FILE = new File("config.yml");

    private YamlDocument document;

    private Configuration() {}

    public void reload() {
        try {
            document = YamlDocument.create(FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            document.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Configuration get() {
        return INSTANCE;
    }

    public @NotNull YamlDocument getConfig() {
        return this.document;
    }

    public String getBotToken() {
        return document.getString("bot-token");
    }

    public long getServerId() {
        return document.getLong("server-id");
    }

    public long getAutoModRuleId() {
        return document.getLong("automod-rule-id");
    }

    public void setAutoModRuleId(long id) {
        document.set("automod-rule-id", id);
        save();
    }

    public long getLogChannelId() {
        return document.getLong("log-channel-id");
    }

    public void setLogChannelId(long id) {
        document.set("log-channel-id", id);
        save();
    }

    public long getSettingsRoleId() {
        return document.getLong("settings-role-id");
    }

    public void addMsgCommand(@NotNull String name, @NotNull String message) {
        document.set("message-commands." + name, message);
        save();
    }

    public void removeMsgCommand(@NotNull String name) {
        document.remove("message-commands." + name);
        save();
    }

    public void addSubmissionCommand(@NotNull String name, long channel) {
        document.set("submission-commands." + name, channel);
        save();
    }

    public void removeSubmissionCommand(@NotNull String name) {
        document.remove("submission-commands." + name);
        save();
    }

}
