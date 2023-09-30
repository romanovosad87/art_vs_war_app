package com.example.artvswar.repository;

import com.example.artvswar.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImagesRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByPublicId(String publicId);
}
