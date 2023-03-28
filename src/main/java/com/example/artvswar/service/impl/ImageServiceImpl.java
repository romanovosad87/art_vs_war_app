package com.example.artvswar.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.example.artvswar.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final AmazonS3 amazonS3;
    @Value("${s3.bucket.name}")
    private String s3BucketName;

    public ImageServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String generateGetUrl(String fileName) {
        if (!amazonS3.doesObjectExist(s3BucketName, fileName))
            return "File does not exist";
        LOG.info("Generating signed URL for file name {}", fileName);
        return generateUrl(fileName, HttpMethod.GET);

    }

    @Override
    public String generatePutUrl(String extension) {
        String fileName = UUID.randomUUID() + extension;
        return generateUrl(fileName, HttpMethod.PUT);
    }

    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1);
        return amazonS3.generatePresignedUrl(s3BucketName, fileName, calendar.getTime(), httpMethod).toString();
    }
}
