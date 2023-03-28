package com.example.artvswar.repository;

import com.example.artvswar.model.Painting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Painting, Long> {
}
