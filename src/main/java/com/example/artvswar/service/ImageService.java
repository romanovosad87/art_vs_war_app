package com.example.artvswar.service;

import com.example.artvswar.dto.response.image.PendingRejectImageResponse;
import com.example.artvswar.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ImageService {

    void updateModerationStatus(String publicId, String status);

    Optional<Image> getImage(String publicId);

    Page<PendingRejectImageResponse> getRejectedImages(Pageable pageable);

    Page<PendingRejectImageResponse> getPendingImages(Pageable pageable);

    void changeModerationStatus(String publicId, String adminUsername, String status);
}
