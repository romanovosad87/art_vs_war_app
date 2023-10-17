package com.example.artvswar.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TimeZoneAPI {
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LOCATION = "location";
    private static final String GMT_OFFSET = "gmt_offset";

    private final RestTemplate restTemplate;

    @Value("${time.zone.api.url}")
    private String timeZoneUrl;

    @Value("${time.zone.api.key}")
    private String timeZoneApiKey;

    public int getOffset(String city, String country) {
        URI uri = UriComponentsBuilder.fromHttpUrl(timeZoneUrl)
                .queryParam(PARAM_API_KEY, timeZoneApiKey)
                .queryParam(PARAM_LOCATION, city + ", " + country)
                .build()
                .toUri();

        JsonNode obj = restTemplate.getForObject(uri, JsonNode.class);
        return Optional.ofNullable(obj).orElseThrow().get(GMT_OFFSET).asInt();
    }

}
