package com.example.artvswar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

@Component
public class CustomAwsCredentialsProvider implements AwsCredentialsProvider {
    @Value("${ses.access.key.id}")
    private String accessKeyId;

    @Value("${ses.access.key.secret}")
    private String accessKeySecret;
    @Override
    public AwsCredentials resolveCredentials() {
        return AwsBasicCredentials.create(accessKeyId, accessKeySecret);
    }
}
