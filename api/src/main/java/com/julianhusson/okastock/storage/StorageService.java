package com.julianhusson.okastock.storage;

import com.julianhusson.okastock.exception.StorageFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.net.URI;


@Service
public class StorageService {

    private final String SCALEWAY_BUCKET_ENDPOINT_URL = "https://s3.fr-par.scw.cloud";

    private final String SCAWELAY_BUCKET_NAME = "okastock-files";
    private final String SCAWELAY_BUCKET_REGION = "fr-par";

    private final String SCALEWAY_ACCESS_KEY = "SCW581RM8PZ8K9ECB4FA";
    private final String SCALEWAY_SECRET_KEY = "0ea39210-e624-4453-b184-1f177fa93560";

    private final S3Client client;


    public StorageService(){
        client = S3Client.builder()
                .region(Region.of(SCAWELAY_BUCKET_REGION))
                .endpointOverride(URI.create(SCALEWAY_BUCKET_ENDPOINT_URL))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(SCALEWAY_ACCESS_KEY, SCALEWAY_SECRET_KEY)))
                .build();
    }

    public void getProfilImage() {
        final GetObjectRequest objectRequest =
                GetObjectRequest
                        .builder()
                        .key("images/profils/profil.jpg")
                        .bucket(SCAWELAY_BUCKET_NAME)
                        .build();
        InputStream file = client.getObject(objectRequest);
        System.out.println(file.readAllBytes());

    }
}
