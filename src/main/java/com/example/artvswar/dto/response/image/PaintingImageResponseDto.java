package com.example.artvswar.dto.response.image;

import com.example.artvswar.model.enumModel.ModerationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class PaintingImageResponseDto {
    private String imagePublicId;
    private String imageUrl;
    private ModerationStatus imageModerationStatus;
    private Set<String> views = new LinkedHashSet<>();
}
