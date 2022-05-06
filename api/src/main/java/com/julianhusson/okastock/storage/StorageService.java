package com.julianhusson.okastock.storage;

import com.julianhusson.okastock.exception.StorageFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;


@Service
public class StorageService {

    @Value("${scaleway.bucket.name}")
    private String SCALEWAY_BUCKET_NAME;

    private final S3Client client;


    public StorageService(
            @Value("${scaleway.bucket.region}") String scalewayBucketRegion,
            @Value("${scaleway.bucket.endpoint-url}") String scalewayBucketEndpointUrl,
            @Value("${scaleway.access-key}") String scalewayAccessKey,
            @Value("${scaleway.secret-key}") String scalewaySecretKey){
        client = S3Client.builder()
                .region(Region.of(scalewayBucketRegion))
                .endpointOverride(URI.create(scalewayBucketEndpointUrl))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(scalewayAccessKey, scalewaySecretKey)))
                .build();
    }

    public String postLogo(MultipartFile logo) {
        String logoURI = UUID.randomUUID().toString();
        final PutObjectRequest objectRequest =
                PutObjectRequest
                        .builder()
                        .key("images/profils/" + logoURI)
                        .acl("public-read")
                        .bucket(SCALEWAY_BUCKET_NAME)
                        .build();
        try {
            byte[] bytes = logo.getBytes();
            client.putObject(objectRequest, RequestBody.fromBytes(bytes));
            return logoURI;
        } catch (IOException e) {
            throw new StorageFileException("Une erreur s'est produite pendant le traitement de votre image.");
        }
    }
}
