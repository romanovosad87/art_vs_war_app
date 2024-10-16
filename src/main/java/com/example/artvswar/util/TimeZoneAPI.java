package com.example.artvswar.util;

import java.net.URI;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeZoneAPI {
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LOCATION = "location";
    private static final String GMT_OFFSET = "gmt_offset";
    private static final String REGEX = "(api_key=)([a-zA-Z0-9]+)";
    public static final String HIDDEN_API_KEY = "api_key=*****";

    private final RestClient restClient;

    @Value("${time.zone.api.url}")
    private String timeZoneUrl;

    @Value("${time.zone.api.key}")
    private String timeZoneApiKey;

    public Integer getOffset(String city, String country) {
        URI uri = getUri(city, country);
        try {
            return Optional.ofNullable(getJsonNodeResponse(uri))
                    .map(resp -> resp.get(GMT_OFFSET).asInt())
                    .orElse(0);
        } catch (Exception e) {
            logError(uri, e);
            return 0;
        }
    }

    private JsonNode getJsonNodeResponse(URI uri) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(JsonNode.class);
    }

    private URI getUri(String city, String country) {
        return UriComponentsBuilder.fromHttpUrl(timeZoneUrl)
                .queryParam(PARAM_API_KEY, timeZoneApiKey)
                .queryParam(PARAM_LOCATION, city + ", " + country)
                .build()
                .toUri();
    }

    private void logError(URI uri, Throwable ex) {
        String preparedForLoggingUri = uri.toString().replaceAll(REGEX, HIDDEN_API_KEY);
        log.error("Can't process request: %s, exception message: [%s]"
                .formatted(preparedForLoggingUri, ex.getMessage()));;
    }
}
