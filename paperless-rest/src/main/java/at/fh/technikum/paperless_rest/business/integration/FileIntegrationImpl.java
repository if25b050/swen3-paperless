package at.fh.technikum.paperless_rest.business.integration;

import at.fh.technikum.paperless_rest.business.exception.S3ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;

import static at.fh.technikum.paperless_rest.business.BusinessUtil.kv;

@Service
@Slf4j
public class FileIntegrationImpl implements FileIntegration {

    private final S3Client s3Client;
    private final String s3Bucket;

    public FileIntegrationImpl(@Value("${s3.bucket}") String s3Bucket,
                               @Value("${s3.url}") String s3Url,
                               @Value("${s3.accesskey}") String s3Accesskey,
                               @Value("${s3.secretkey}") String s3Secretkey) {

        this.s3Bucket = s3Bucket;

        // create the Client
        try {
            s3Client = S3Client.builder()
                    .endpointOverride(URI.create(s3Url))
                    .region(Region.US_EAST_1) // RustFS default region
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(s3Accesskey, s3Secretkey)
                            )
                    )
                    // RustFS uses path-style URLs by default; virtual-host style requires RUSTFS_SERVER_DOMAINS
                    .forcePathStyle(true)
                    .build();

            log.info("Connection to S3 established");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Create bucket
        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(s3Bucket).build());
            System.out.println("Bucket created: " + s3Bucket);
        } catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException e) {
            System.out.println("Bucket \"" + s3Bucket + "\" already exists.");
        }
    }

    @Override
    public void getFile(UUID documentId, OutputStream fileTarget) {
        log.debug("The file behind {} will be downloaded", documentId);

        try (ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                GetObjectRequest.builder().bucket(s3Bucket).key(documentId.toString()).build()
        )) {
            response.transferTo(fileTarget);
            log.info("The file behind {} was downloaded", documentId);
        } catch (IOException e) {
            log.error("The file download behind {} failed", documentId);
            throw new RuntimeException("Document download failed!");
        } catch (NoSuchKeyException ex) {
            log.warn(ex.getMessage());
            throw new S3ObjectNotFoundException("Document-File \"" + documentId + "\" not found!");
        }
    }

    @Override
    public void saveFile(UUID documentId, byte[] file) {
        log.debug("A file will be saved under {}", kv("key", documentId));

        s3Client.putObject(
                PutObjectRequest.builder().bucket(s3Bucket).key(documentId.toString()).build(),
                RequestBody.fromBytes(file)
        );

        log.info("A file was saved under {}", kv("key", documentId));
    }

    @Override
    public void deleteFile(UUID documentId) {
        log.debug("The file behind {} will be deleted", documentId);

        s3Client.deleteObject(
                DeleteObjectRequest.builder().bucket(s3Bucket).key(documentId.toString()).build()
        );

        log.info("The file behind {} was deleted", documentId);

    }
}
