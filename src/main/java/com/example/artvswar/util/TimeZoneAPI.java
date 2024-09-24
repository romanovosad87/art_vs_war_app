package com.example.artvswar.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeZoneAPI {
    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LOCATION = "location";
    private static final String GMT_OFFSET = "gmt_offset";
    private static final String REGEX = "(api_key=)([a-zA-Z0-9]+)";
    public static final String HIDDEN_API_KEY = "api_key=*****";

    private final WebClient webClient;

    @Value("${time.zone.api.url}")
    private String timeZoneUrl;

    @Value("${time.zone.api.key}")
    private String timeZoneApiKey;

    public Mono<Integer> getOffset(String city, String country) {
        URI uri = UriComponentsBuilder.fromHttpUrl(timeZoneUrl)
                .queryParam(PARAM_API_KEY, timeZoneApiKey)
                .queryParam(PARAM_LOCATION, city + ", " + country)
                .build()
                .toUri();

        return getJsonNodeResponse(uri.toString())
                .map(response -> response.get(GMT_OFFSET).asInt())
                .doOnError(ex -> {
                    String preparedForLoggingUri = uri.toString().replaceAll(REGEX, HIDDEN_API_KEY);
                    log.error("Can't process request: %s, exception message: [%s]"
                            .formatted(preparedForLoggingUri, ex.getMessage()));
                })
                .onErrorReturn(0);
    }

    private Mono<JsonNode> getJsonNodeResponse(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}
