package com.example.artvswar.repository.painting;

import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.model.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PaintingRepository extends JpaRepository<Painting, Long>,
        JpaSpecificationExecutor<Painting>, CustomPaintingRepository {

    <T> Page<T> findAllByAuthorPrettyId(Class<T> type, String authorPrettyId, Pageable pageable);

    <T> Page<T> findAllByAuthorCognitoSubject(Class<T> type, String cognitoSubject,
                                              Pageable pageable);

    <T> Page<T> findAllByCollectionPrettyId(Class<T> type, String collectionPrettyId,
                                            Pageable pageable);

    Optional<Painting> findByPrettyId(String prettyId);

    @Query("select count(p.id) from Painting p where p.prettyId = ?1")
    int checkIfExistPrettyId(String prettyId);

    @Query("select a.prettyId from Painting p inner join p.author a where p.prettyId = ?1")
    String getAuthorPrettyId(String paintingPrettyId);

    @Query("select new com.example.artvswar.dto.response.image.AdditionalImageResponseDto(ai.id, im.publicId, im.url, im.moderationStatus) "
            + "from Painting p left join p.additionalImages ai join ai.image im where p.prettyId =?1")
    List<AdditionalImageResponseDto> getAdditionalImagesByPrettyId(String prettyId);

    void deleteByPrettyId(String prettyId);

    @Query(value = "("
            + "    SELECT p.pretty_id, pi.transformed_ratio, a.pretty_id as api, i.url"
            + "    FROM paintings p"
            + "    JOIN painting_images pi"
            + "    ON p.id = pi.id"
            + "    JOIN images i"
            + "    ON pi.image_id = i.id"
            + "    JOIN authors a"
            + "    ON p.author_id = a.id"
            + "    JOIN stripe_profiles sp"
            + "    ON a.id = sp.id"
            + "    JOIN author_shipping_addresses asa"
            + "    ON a.id = asa.author_id"
            + "    WHERE a.is_deleted = false"
            + "    AND i.moderation_status = 20"
            + "    AND sp.is_details_submitted = true"
            + "    AND asa.author_id is not null"
            + "    AND pi.transformed_ratio = :firstRatio"
            + "    ORDER BY RAND()"
            + "    LIMIT :firstAmount"
            + ")"
            + "UNION ALL"
            + "("
            + "    SELECT p.pretty_id, pi.transformed_ratio, a.pretty_id as api, i.url"
            + "    FROM paintings p"
            + "    JOIN painting_images pi"
            + "    ON p.id = pi.id"
            + "    JOIN images i"
            + "    ON pi.image_id = i.id"
            + "    JOIN authors a"
            + "    ON p.author_id = a.id"
            + "    JOIN stripe_profiles sp"
            + "    ON a.id = sp.id"
            + "    JOIN author_shipping_addresses asa"
            + "    ON a.id = asa.author_id"
            + "    WHERE a.is_deleted = false"
            + "    AND i.moderation_status = 20"
            + "    AND sp.is_details_submitted = true"
            + "    AND asa.author_id is not null"
            + "    AND pi.transformed_ratio = :secondRatio"
            + "    ORDER BY RAND()"
            + "    LIMIT :secondAmount"
            + ")"
            + "UNION ALL"
            + "("
            + "    SELECT p.pretty_id, pi.transformed_ratio, a.pretty_id as api, i.url"
            + "    FROM paintings p"
            + "    JOIN painting_images pi"
            + "    ON p.id = pi.id"
            + "    JOIN images i"
            + "    ON pi.image_id = i.id"
            + "    JOIN authors a"
            + "    ON p.author_id = a.id"
            + "    JOIN stripe_profiles sp"
            + "    ON a.id = sp.id"
            + "    JOIN author_shipping_addresses asa"
            + "    ON a.id = asa.author_id"
            + "    WHERE a.is_deleted = false"
            + "    AND i.moderation_status = 20"
            + "    AND sp.is_details_submitted = true"
            + "    AND asa.author_id is not null"
            + "    AND pi.transformed_ratio = :thirdRatio"
            + "    ORDER BY RAND()"
            + "    LIMIT :thirdAmount"
            + ")"
            + "UNION ALL"
            + "("
            + "    SELECT p.pretty_id, pi.transformed_ratio, a.pretty_id as api, i.url"
            + "    FROM paintings p"
            + "    JOIN painting_images pi"
            + "    ON p.id = pi.id"
            + "    JOIN images i"
            + "    ON pi.image_id = i.id"
            + "    JOIN authors a"
            + "    ON p.author_id = a.id"
            + "    JOIN stripe_profiles sp"
            + "    ON a.id = sp.id"
            + "    JOIN author_shipping_addresses asa"
            + "    ON a.id = asa.author_id"
            + "    WHERE a.is_deleted = false"
            + "    AND i.moderation_status = 20"
            + "    AND sp.is_details_submitted = true"
            + "    AND asa.author_id is not null"
            + "    AND pi.transformed_ratio = :fourthRatio"
            + "    ORDER BY RAND()"
            + "    LIMIT :fourthAmount"
            + ")"
            + "UNION ALL"
            + "("
            + "    SELECT p.pretty_id, pi.transformed_ratio, a.pretty_id as api, i.url"
            + "    FROM paintings p"
            + "    JOIN painting_images pi"
            + "    ON p.id = pi.id"
            + "    JOIN images i"
            + "    ON pi.image_id = i.id"
            + "    JOIN authors a"
            + "    ON p.author_id = a.id"
            + "    JOIN stripe_profiles sp"
            + "    ON a.id = sp.id"
            + "    JOIN author_shipping_addresses asa"
            + "    ON a.id = asa.author_id"
            + "    WHERE a.is_deleted = false"
            + "    AND i.moderation_status = 20"
            + "    AND sp.is_details_submitted = true"
            + "    AND asa.author_id is not null"
            + "    AND pi.transformed_ratio = :fifthRatio"
            + "    ORDER BY RAND()"
            + "    LIMIT :fifthAmount"
            + ")"
            + "UNION ALL"
            + "("
            + "    SELECT p.pretty_id, pi.transformed_ratio, a.pretty_id as api, i.url"
            + "    FROM paintings p"
            + "    JOIN painting_images pi"
            + "    ON p.id = pi.id"
            + "    JOIN images i"
            + "    ON pi.image_id = i.id"
            + "    JOIN authors a"
            + "    ON p.author_id = a.id"
            + "    JOIN stripe_profiles sp"
            + "    ON a.id = sp.id"
            + "    JOIN author_shipping_addresses asa"
            + "    ON a.id = asa.author_id"
            + "    WHERE a.is_deleted = false"
            + "    AND i.moderation_status = 20"
            + "    AND sp.is_details_submitted = true"
            + "    AND asa.author_id is not null"
            + "    AND pi.transformed_ratio = :sixthRatio"
            + "    ORDER BY RAND()"
            + "    LIMIT :sixthAmount)", nativeQuery = true)
    List<Object[]> findPaintingsForMainPage(@Param("firstRatio") double firstRatio,
                                            @Param("firstAmount") int firstAmount,
                                            @Param("secondRatio") double secondRatio,
                                            @Param("secondAmount") int secondAmount,
                                            @Param("thirdRatio") double thirdRatio,
                                            @Param("thirdAmount") int thirdAmount,
                                            @Param("fourthRatio") double fourthRatio,
                                            @Param("fourthAmount") int fourthAmount,
                                            @Param("fifthRatio") double fifthRatio,
                                            @Param("fifthAmount") int fifthAmount,
                                            @Param("sixthRatio") double sixthRatio,
                                            @Param("sixthAmount") int sixthAmount);
}
