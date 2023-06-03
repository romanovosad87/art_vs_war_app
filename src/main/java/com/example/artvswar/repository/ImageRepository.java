package com.example.artvswar.repository;

import com.example.artvswar.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {
    @Query(value = "select * from images where transformed_ratio in ?1 order by rand() limit ?2", nativeQuery = true)
    List<Image> findByTransformedRatio(double ratio, int amount);

    @Query(value = "(\n"
            + "    SELECT * \n"
            + "    FROM images \n"
            + "    WHERE transformed_ratio = :firstRatio \n"
            + "    ORDER BY RAND() \n"
            + "    LIMIT :firstAmount\n"
            + ")\n"
            + "UNION ALL\n"
            + "(\n"
            + "    SELECT * \n"
            + "    FROM images \n"
            + "    WHERE transformed_ratio = :secondRatio \n"
            + "    ORDER BY RAND() \n"
            + "    LIMIT :secondAmount\n"
            + ")\n"
            + "UNION ALL\n"
            + "(\n"
            + "    SELECT * \n"
            + "    FROM images \n"
            + "    WHERE transformed_ratio = :thirdRatio \n"
            + "    ORDER BY RAND() \n"
            + "    LIMIT :thirdAmount\n"
            + ")", nativeQuery = true)
    List<Image> findByTransformedRatioAndLimit(@Param("firstRatio") double firstRatio, @Param("firstAmount") int firstAmount,
                                               @Param("secondRatio") double secondRatio, @Param("secondAmount") int secondAmount,
                                               @Param("thirdRatio") double thirdRatio, @Param("thirdAmount") int thirdAmount);


}
