package com.example.artvswar.dto.response.image;

import com.example.artvswar.model.enumModel.ModerationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RejectAssetsResponse {
    private String publicId;
    private String createdAt;
    private String url;
    private ModerationStatus moderationStatus;
}
