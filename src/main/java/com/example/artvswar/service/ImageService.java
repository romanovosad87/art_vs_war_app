package com.example.artvswar.service;

import com.example.artvswar.model.Image;
import java.util.Optional;

public interface ImageService {

    void updateModerationStatus(String publicId, String status);

    Optional<Image> getImage(String publicId);
}
