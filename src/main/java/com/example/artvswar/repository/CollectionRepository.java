package com.example.artvswar.repository;

import com.example.artvswar.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    @Query("select count(c.id) from Collection c where c.prettyId = ?1")
    int checkIfExistPrettyId(String prettyId);

    <T> Page<T> findAllByAuthorPrettyId(Class<T> type, String authorPrettyId, Pageable pageable);
    <T> Page<T> findAllByAuthorCognitoSubject(Class<T> type, String cognitoSubject, Pageable pageable);

    Optional<Collection> findByPrettyId(String prettyId);

    <T> T getByPrettyId(Class<T> type, String prettyId);

    void deleteByPrettyId(String prettyId);
}
