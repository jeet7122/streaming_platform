package com.streaming_platform.upload_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * Configuration class for external integrations used in Upload Service.
 *
 * <p>This configuration provides:
 * <ul>
 *     <li>Amazon S3-compatible client (used here for Cloudflare R2 or similar object storage)</li>
 *     <li>RestTemplate bean for inter-service HTTP communication</li>
 * </ul>
 *
 * <p><b>R2/S3 Notes:</b>
 * <ul>
 *     <li>Uses custom endpoint (e.g., Cloudflare R2)</li>
 *     <li>Requires access key and secret key for authentication</li>
 *     <li>Region is required by SDK but may not strictly apply to R2</li>
 * </ul>
 */
@Configuration
public class R2Config {

    /**
     * Access key for S3-compatible storage (e.g., Cloudflare R2).
     */
    @Value("${R2.access-key}")
    private String accessKey;

    /**
     * Secret key for S3-compatible storage.
     */
    @Value("${R2.secret-key}")
    private String secretKey;

    /**
     * Endpoint URL for the S3-compatible service (e.g., R2 endpoint).
     */
    @Value("${R2.endpoint}")
    private String endpoint;

    /**
     * Creates and configures an {@link S3Client} bean.
     *
     * <p>This client is used for interacting with object storage,
     * such as uploading video files and retrieving them.
     *
     * @return configured S3Client instance
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                // Override default AWS endpoint with custom R2 endpoint
                .endpointOverride(URI.create(endpoint))

                // Provide static credentials for authentication
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )

                // Region is required by SDK; may be arbitrary for R2
                .region(Region.US_EAST_1)

                .build();
    }

    /**
     * Creates a {@link RestTemplate} bean for making HTTP calls to other services.
     *
     * <p>Used for communication between microservices (e.g., Upload Service → Video Service).
     *
     * @return RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}