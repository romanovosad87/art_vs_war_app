package com.example.artvswar.dto.response.author;

import lombok.Data;

@Data
public class AuthorCheckStripeAndAddressPresenceResponseDto {

    private boolean hasStripeProfile;
    private boolean hasAddress;

    public AuthorCheckStripeAndAddressPresenceResponseDto(boolean hasStripeProfile) {
        this.hasStripeProfile = hasStripeProfile;
    }
}
