package com.example.artvswar.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonCognitoConfig {
    @Value("${cognito.access.key.id}")
    private String accessKeyId;

    @Value("${cognito.access.key.secret}")
    private String accessKeySecret;

    @Value("${cognito.region.name}")
    private String cognitoRegionName;

    @Bean
    public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId,
                accessKeySecret);

        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(cognitoRegionName)
                .build();
    }
}
