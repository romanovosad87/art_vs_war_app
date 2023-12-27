package com.example.artvswar.dto.response.image;

import com.example.artvswar.model.enumModel.ModerationStatus;
import com.example.artvswar.util.gson.JsonLocalDateTimeImageAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PendingRejectImageResponse {
    private String publicId;
    @JsonAdapter(JsonLocalDateTimeImageAdapter.class)
    private LocalDateTime createdAt;
    private String url;
    private ModerationStatus moderationStatus;
}
