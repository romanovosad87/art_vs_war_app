package com.example.artvswar.repository;

import com.example.artvswar.model.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaintingRepository extends JpaRepository<Painting, Long>,
        JpaSpecificationExecutor<Painting> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"author", "style", "medium", "support"}
    )
    Page<Painting> findAll(Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"author", "style", "medium", "support"}
    )
    Optional<Painting> findById(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"author", "style", "medium", "support"}
    )
    Page<Painting> findAll(Specification<Painting> specification, Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"author", "style", "medium", "support"}
    )
    Page<Painting> findPaintingByAuthorId(String id, Pageable pageable);
}
