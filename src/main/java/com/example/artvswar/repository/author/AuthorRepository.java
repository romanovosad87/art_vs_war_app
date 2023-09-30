package com.example.artvswar.repository.author;

import com.example.artvswar.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long>, CustomAuthorRepository {

    <T> Optional<T> findByCognitoSubject(Class<T> type, String cognitoSubject);

    <T> Optional<T> findByPrettyId(Class<T> type, String prettyId);

    @Query("select count(a.cognitoSubject) from Author a where a.prettyId = ?1")
    int checkIfExistPrettyId(String prettyId);

    void deleteByCognitoSubject(String cognitoSubject);
}
