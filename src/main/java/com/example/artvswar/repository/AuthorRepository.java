package com.example.artvswar.repository;

import com.example.artvswar.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, String> {
    <T> Page<T> findAllBy(Class<T> type, Pageable pageable);

    <T> Optional<T> findByCognitoUsername(Class<T> type, String cognitoUsername);
}
