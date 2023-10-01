package com.example.artvswar.repository;

import com.example.artvswar.model.ArtProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ArtProcessRepository extends JpaRepository<ArtProcess, Long> {
    <T> List<T> findByAuthorPrettyId(Class<T> type, String authorPrettyId);

    <T> List<T> findByAuthorCognitoSubject(Class<T> type, String cognitoSubject);

    <T> Optional<T> findById(Class<T> type, Long id);
}
