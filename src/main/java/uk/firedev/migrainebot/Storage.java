package uk.firedev.migrainebot;

import net.dv8tion.jda.api.entities.Message;
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

public class Storage {

    private static final String ACCESS_URL = "https://r2.firedev.uk/";

    private static final S3Client client = initS3();

    private static S3Client initS3() {
        String endpoint = "https://%s.r2.cloudflarestorage.com".formatted("fec022a5d218bfb5ef54338a9ce2ebb9");
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
            Configuration.get().getS3AccessKey(),
            Configuration.get().getS3SecretKey()
        );
        S3Configuration configuration = S3Configuration.builder()
            .pathStyleAccessEnabled(true)
            .chunkedEncodingEnabled(false)
            .build();

        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of("auto"))
            .serviceConfiguration(configuration)
            .build();
    }

    private Storage() {}

    public static String saveAttachment(Message.Attachment attachment) {
        if (!attachment.isImage()) {
            throw new IllegalArgumentException("Attachment must be an image.");
        }
        byte[] bytes;
        try (InputStream in = attachment.getProxy().download().join()) {
            bytes = in.readAllBytes();
        } catch (IOException | CompletionException exception) {
            throw new RuntimeException("Could not download Discord attachment", exception.getCause());
        }
        String key = "attachment-" + UUID.randomUUID();
        String contentType = attachment.getContentType();
        client.putObject(
            PutObjectRequest.builder()
                .bucket("wzwupdates-submissions")
                .key(key)
                .contentType(contentType)
                .build(),
            RequestBody.fromBytes(bytes)
        );
        return ACCESS_URL + key;
    }

}
