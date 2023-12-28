package com.example.artvswar.dto.response.image;

import com.example.artvswar.dto.response.author.AuthorImageModerationResponseDto;
import com.example.artvswar.model.enumModel.ModerationStatus;
import com.example.artvswar.util.gson.JsonLocalDateTimeImageAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PendingRejectImageResponse {
    private String publicId;
    @JsonAdapter(JsonLocalDateTimeImageAdapter.class)
    private LocalDateTime createdAt;
    private String url;
    private ModerationStatus moderationStatus;
    private AuthorImageModerationResponseDto author;

    public PendingRejectImageResponse(String publicId, LocalDateTime createdAt, String url,
                                      ModerationStatus moderationStatus) {
        this.publicId = publicId;
        this.createdAt = createdAt;
        this.url = url;
        this.moderationStatus = moderationStatus;
    }
}
