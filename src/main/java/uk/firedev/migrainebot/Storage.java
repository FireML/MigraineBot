package uk.firedev.migrainebot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.Nullable;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import uk.firedev.migrainebot.config.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;

public class Storage {

    private static final String BUCKET = "wzwupdates-submissions";
    private static final String ACCESS_URL = "https://r2.firedev.uk/";

    private static S3Client client = null;

    public static void initS3() {
        String endpoint = "https://%s.r2.cloudflarestorage.com".formatted("fec022a5d218bfb5ef54338a9ce2ebb9");
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
            Configuration.get().getS3AccessKey(),
            Configuration.get().getS3SecretKey()
        );
        S3Configuration configuration = S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .chunkedEncodingEnabled(false)
            .build();

        client = S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of("auto"))
            .serviceConfiguration(configuration)
            .build();
    }

    private Storage() {}

    public static @Nullable String saveAttachment(Message.Attachment attachment, InteractionHook interaction) {
        if (client == null) {
            throw new IllegalStateException("S3Client is null.");
        }
        if (!attachment.isImage()) {
            interaction.sendMessage("Attachment must be an image.").setEphemeral(true).queue();
            return null;
        }
        byte[] bytes;
        try (InputStream in = attachment.getProxy().download().join()) {
            bytes = in.readAllBytes();
        } catch (IOException | CompletionException exception) {
            interaction.sendMessage("Failed to save attachment. Please try again.").setEphemeral(true).queue();
            Main.getLogger().log(Level.SEVERE, "Could not download Discord Attachment.", exception);
            return null;
        }
        String key = "attachment-" + UUID.randomUUID();
        String contentType = attachment.getContentType();
        client.putObject(
            PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(key)
                .contentType(contentType)
                .build(),
            RequestBody.fromBytes(bytes)
        );
        return ACCESS_URL + key;
    }

}
