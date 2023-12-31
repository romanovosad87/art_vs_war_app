package com.example.artvswar.repository;

import com.example.artvswar.dto.response.artprocess.ArtProcessResponseDto;
import com.example.artvswar.model.ArtProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ArtProcessRepository extends JpaRepository<ArtProcess, Long> {

    @Query("select new com.example.artvswar.dto.response.artprocess.ArtProcessResponseDto("
            + "ap.id, "
            + "ap.description, "
            + "ap.artProcessImage.image.publicId, "
            + "ap.artProcessImage.image.url, "
            + "ap.artProcessImage.image.moderationStatus, "
            + "ap.artProcessImage.width, "
            + "ap.artProcessImage.height) "
            + "from ArtProcess ap where ap.author.prettyId = ?1 "
            + "and ap.artProcessImage.image.moderationStatus = 20")
    List<ArtProcessResponseDto> findByAuthorPrettyId(String authorPrettyId);

    <T> List<T> findByAuthorCognitoSubject(Class<T> type, String cognitoSubject);

    <T> Optional<T> findById(Class<T> type, Long id);
}
