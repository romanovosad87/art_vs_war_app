package com.example.artvswar.repository;

import com.example.artvswar.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
    @Modifying
    @Query(value = "insert into authors(id) values (:id)", nativeQuery = true)
    void saveAuthorById(@Param("id") String id);
}
