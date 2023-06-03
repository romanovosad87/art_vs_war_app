package com.example.artvswar.repository.painting;

import com.example.artvswar.model.Painting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.Optional;

public interface PaintingRepository extends JpaRepository<Painting, Long>,
        JpaSpecificationExecutor<Painting>, CustomPaintingRepository {

    @Query("select max(price) from Painting")
    Optional<BigDecimal> getMaxPrice();

    @Query("select min(price) from Painting")
    Optional<BigDecimal> getMinPrice();

    @Query("select max(width) from Painting ")
    Optional<Integer> getMaxWidth();

    @Query("select max(height) from Painting ")
    Optional<Integer> getMaxHeight();
}
