package com.example.artvswar.repository;

import com.example.artvswar.model.Image;
import com.example.artvswar.model.enummodel.ModerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ImagesRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByPublicId(String publicId);
    <T> Page<T> getAllByModerationStatus(Class<T> clazz, ModerationStatus status, Pageable pageable);
}
