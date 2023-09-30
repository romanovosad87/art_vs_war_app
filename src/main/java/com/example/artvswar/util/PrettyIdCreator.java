package com.example.artvswar.util;

import org.springframework.stereotype.Component;

@Component
public class PrettyIdCreator {
    private static final String SPECIAL_CHARACTERS = "[/!']";
    private static final String MULTI_SPACE_REGEX = "\\s+";

    public String create(String input) {
        return input.trim()
                .toLowerCase()
                .replaceAll(SPECIAL_CHARACTERS, "")
                .replaceAll(MULTI_SPACE_REGEX, " ")
                .replace(" ", "-");
    }
}
