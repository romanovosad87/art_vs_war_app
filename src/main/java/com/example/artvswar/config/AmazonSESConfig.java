package com.example.artvswar.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import io.awspring.cloud.ses.SimpleEmailServiceJavaMailSender;
import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
@RequiredArgsConstructor
public class AmazonSESConfig {
    @Value("${ses.access.key.id}")
    private String accessKeyId;

    @Value("${ses.access.key.secret}")
    private String accessKeySecret;

    @Value("${ses.region.name}")
    private String regionName;
    private final CustomAwsCredentialsProvider awsCredentialsProvider;

    @Bean
    public AmazonSimpleEmailService getAmazonSESClient() {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId,
                accessKeySecret);

        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(regionName)
                .build();
    }

    @Bean
    public MailSender mailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
        return new SimpleEmailServiceMailSender(amazonSimpleEmailService);
    }

    @Bean
    public JavaMailSender javaMailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
        return new SimpleEmailServiceJavaMailSender(amazonSimpleEmailService);
    }

    @Bean
    public SesV2Client getSesV2Client() {
        return SesV2Client.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.EU_WEST_3)
                .build();
    }

    @Bean
    public SesClient getSesClient() {
        return SesClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.EU_WEST_3)
                .build();
    }
}
