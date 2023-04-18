package com.example.artvswar.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.PEM;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.example.artvswar.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final AmazonS3 amazonS3;
    @Value("${s3.bucket.name}")
    private String s3BucketName;
    @Value("${cloud.front.distribution.domain}")
    String distributionDomain;
    @Value("${cloud.front.key.pair.id}")
    private String keyPairId;
    @Value("${cloud.front.private.key.filePath}")
    private Resource privateKeyFilePath;

    public ImageServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String generateGetUrl(String fileName) {

        Calendar timeExpiration = Calendar.getInstance();
        timeExpiration.setTime(new Date());
        timeExpiration.add(Calendar.MINUTE, 60);

        try {
            LOG.info("Generating signed GET URL for file name {}", fileName);

            InputStream inputStream = privateKeyFilePath.getInputStream();
            PrivateKey privateKey = PEM.readPrivateKey(inputStream);
            String resourcePath = SignerUtils.generateResourcePath(SignerUtils.Protocol.https,
                    distributionDomain, fileName);
            return CloudFrontUrlSigner.getSignedURLWithCannedPolicy(resourcePath, keyPairId,
                    privateKey, timeExpiration.getTime());
        } catch (Exception e) {
            throw new RuntimeException("Can't generate signed URL", e);
        }
    }

    @Override
    public Map<String, String> generatePutUrl(String extension) {
        String fileName = UUID.randomUUID() + "." + extension;
        LOG.info("Generating pre-signed PUT URL for file name {}", fileName);
        return Map.of("imageFileName", fileName,
                "imagePutUrl", generateUrl(fileName, HttpMethod.PUT));
    }

    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10);
        return amazonS3.generatePresignedUrl(s3BucketName, fileName, calendar.getTime(),
                httpMethod).toString();
    }
}
