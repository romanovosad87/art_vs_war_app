package com.example.artvswar.repository.paintingImage;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.PaintingImage;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomPaintingImageRepositoryImpl
        implements CustomPaintingImageRepository {
    private final EntityManager entityManager;
    @Override
    public PaintingImage getByIdWithCustomQuery(Long id) {
        PaintingImage paintingImage;
        try {
            paintingImage = entityManager.createQuery("select distinct pi from PaintingImage pi "
                                    + "join fetch pi.roomViews join fetch pi.image where pi.id = ?1",
                            PaintingImage.class)
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .setParameter(1, id)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new AppEntityNotFoundException(String.format("Can't find paintingImage by id = %s", id));
        }
        return paintingImage;
    }
}
