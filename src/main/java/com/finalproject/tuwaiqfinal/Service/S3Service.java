package com.finalproject.tuwaiqfinal.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3;
    @Value("${aws.s3.bucket}")
    private String bucket;

    public String uploadFile(String key, MultipartFile file) throws IOException {
        log.info("Uploading file: {} ({} bytes)", key, file.getSize());
        s3.putObject(
                PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build(),
                RequestBody.fromInputStream(file.getInputStream(),file.getSize())
        );
        return publicUrl(bucket, key);
    }

    public String uploadBytes(String key, byte[] data, String contentType) {
        log.info("Uploading bytes: {} ({} bytes)", key, data.length);
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromBytes(data)
        );
        return publicUrl(bucket, key);
    }


    public byte[] downloadBytes(String key) {
        log.info("Downloading bytes: {}", key);
        return s3.getObject(
                GetObjectRequest.builder().bucket(bucket).key(key).build(),
                ResponseTransformer.toBytes()
        ).asByteArray();
    }

    private String publicUrl(String bucket, String key) {
        // Safer than manual string formatting; respects region/client settings
        URL url = s3.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
        return url.toString();
    }
}
