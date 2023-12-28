package com.example.artvswar.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ImagePublicIdParser {
    private static final String REGEX = "/images/(\\w+-\\w+-\\w+-\\w+-\\w+)/";

    public String getCognitoSubject(String publicId) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(publicId);
        String result = null;
        if (matcher.find()) {
            result = matcher.group(1);
        } else {
            log.error("No match found found for public id: {}", publicId);
        }
        return result;
    }
}
