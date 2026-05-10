package uk.firedev.migrainebot.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A super lazy way to configure things. Just create a file for everything :D
 */
public class Config {

    public static @NotNull String read(@NotNull String name) {
        Path path = Paths.get(name);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            return Files.readString(path, StandardCharsets.UTF_8).trim();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void write(@NotNull String name, @NotNull String data) {
        Path path = Paths.get(name);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.writeString(path, data, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static @NotNull String getBotToken() {
        return read("discord_token");
    }

}
