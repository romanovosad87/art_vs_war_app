package com.example.artvswar.repository.painting;

import com.example.artvswar.dto.response.MediumResponseDto;
import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.dto.response.SubjectResponseDto;
import com.example.artvswar.dto.response.SupportResponseDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.image.ImageResponse;
import com.example.artvswar.dto.response.painting.PaintingDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.Medium;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.Subject;
import com.example.artvswar.model.Support;
import com.example.artvswar.repository.painting.CustomPaintingRepository;
import com.example.artvswar.util.image.ImageTransformation;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.Query;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomPaintingRepositoryImpl
        implements CustomPaintingRepository {
    private static final String AUTHOR = "author";
    private static final String IMAGE = "image";
    private static final String STYLES = "styles";
    private static final String MEDIUMS = "mediums";
    private static final String SUPPORTS = "supports";
    private static final String SUBJECTS = "subjects";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String YEAR_OF_CREATION = "yearOfCreation";
    private static final String COGNITO_USERNAME = "cognitoUsername";
    private static final String FULL_NAME = "fullName";
    private static final String TRANSFORMED_RATIO = "transformedRatio";
    private static final String NAME = "name";
    private static final String PAINTING_ID = "painting_id";
    private static final String PAINTING_TITLE = "painting_title";
    private static final String PAINTING_DESCRIPTION = "painting_description";
    private static final String PAINTING_PRICE = "painting_price";
    private static final String PAINTING_HEIGHT = "painting_height";
    private static final String PAINTING_WIDTH = "painting_width";
    private static final String PAINTING_YEAR_OF_CREATION = "painting_yearOfCreation";
    private static final String AUTHOR_ID = "author_id";
    private static final String AUTHOR_FULL_NAME = "author_fullName";
    private static final String IMAGE_ID = "image_id";
    private static final String IMAGE_RATIO = "image_ratio";
    private static final String STYLE_ID = "style_id";
    private static final String STYLE_NAME = "style_name";
    private static final String MEDIUM_ID = "medium_id";
    private static final String MEDIUM_NAME = "medium_name";
    private static final String SUPPORT_ID = "support_id";
    private static final String SUPPORT_NAME = "support_name";
    private static final String SUBJECT_ID = "subject_id";
    private static final String SUBJECT_NAME = "subject_name";
    private final EntityManager entityManager;
    private final ImageTransformation imageTransformation;

    @Override
    public Page<PaintingDto> getAllDtos(Pageable pageable) {

//        Map<Long, PaintingDto> paintingDtoMap = new LinkedHashMap<>();
//
//
//        String query = "select p.id as painting_id, "
//                + "p.title as painting_title, "
//                + "p.description as painting_description, "
//                + "p.price as painting_price, "
//                + "p.width as painting_width, "
//                + "p.height as painting_height, "
//                + "p.yearOfCreation as painting_yearOfCreation, "
//                + "p.imageFileName as painting_imageFileName, "
//                + "a.cognitoUsername as author_id, "
//                + "a.fullName as author_fullName "
//                + "from Painting p "
//                + "join p.author a";
//
//        String ordering;
//        if (pageable.getSort().isSorted()) {
//            ordering = " ORDER BY " + pageable.getSort().stream().map(order -> "p." + order.getProperty() + " " + order.getDirection())
//                    .collect(Collectors.joining(", "));
//        } else {
//            ordering = " ORDER BY p.id DESC";
//        }
//
//        int pageSize = pageable.getPageSize();
//        int offset = (int) pageable.getOffset();
//
//        Stream<Tuple> authorStyleStream = entityManager.createQuery(query + ordering, Tuple.class)
//                .setFirstResult(offset)
//                .setMaxResults(pageSize)
//                .getResultStream();
//
//        authorStyleStream.map(tuple -> {
//                    PaintingDto paintingDto = paintingDtoMap.computeIfAbsent(tuple.get("painting_id", Long.class),
//                            id -> new PaintingDto(tuple.get("painting_id", Long.class),
//                                    tuple.get("painting_title", String.class),
//                                    tuple.get("painting_description", String.class),
//                                    tuple.get("painting_price", BigDecimal.class),
//                                    tuple.get("painting_height", Double.class),
//                                    tuple.get("painting_width", Double.class),
//                                    tuple.get("painting_yearOfCreation", Integer.class)));
//
//                    paintingDto.setAuthor(new AuthorForPaintingResponseDto(tuple.get("author_id", String.class),
//                            tuple.get("author_fullName", String.class)));
//
//                    return paintingDto;
//                })
//                .collect(Collectors.toList());
//
//        String longsId = "";
//        if (!paintingDtoMap.keySet().isEmpty()) {
//            longsId = " where p.id in ("
//                    + paintingDtoMap.keySet().stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.joining(", "))
//                    + ")";
//        }
//
//        Stream<Tuple> styleStream = entityManager.createQuery("select p.id as painting_id, "
//                        + "st.id as style_id, "
//                        + "st.name as style_name "
//                        + "from Painting p "
//                        + "join p.styles st"
//                        + longsId, Tuple.class)
//                .getResultStream();
//
//        styleStream.map(tuple -> paintingDtoMap.computeIfPresent(tuple.get("painting_id", Long.class),
//                        (id, dto) -> {
//                            dto.getMediums().add(new MediumResponseDto(tuple.get("style_id", Long.class),
//                                    tuple.get("style_name", String.class)));
//                            return dto;
//                        }))
//                .distinct()
//                .collect(Collectors.toList());
//
//        Stream<Tuple> mediumStream = entityManager.createQuery("select p.id as painting_id, "
//                        + "m.id as medium_id, "
//                        + "m.name as medium_name "
//                        + "from Painting p "
//                        + "join p.mediums m"
//                        + longsId, Tuple.class)
//                .getResultStream();
//
//        mediumStream.map(tuple -> paintingDtoMap.computeIfPresent(tuple.get("painting_id", Long.class),
//                        (id, dto) -> {
//                            dto.getMediums().add(new MediumResponseDto(tuple.get("medium_id", Long.class),
//                                    tuple.get("medium_name", String.class)));
//                            return dto;
//                        }))
//                .distinct()
//                .collect(Collectors.toList());
//
//        Stream<Tuple> supportStream = entityManager.createQuery("select p.id as painting_id, "
//                        + "sp.id as support_id, "
//                        + "sp.name as support_name "
//                        + "from Painting p "
//                        + "join p.supports sp"
//                        + longsId, Tuple.class)
//                .getResultStream();
//
//        supportStream.map(tuple -> paintingDtoMap.computeIfPresent(tuple.get("painting_id", Long.class),
//                        (id, dto) -> {
//                            dto.getSupports().add(new SupportResponseDto(tuple.get("support_id", Long.class),
//                                    tuple.get("support_name", String.class)));
//                            return dto;
//                        }))
//                .distinct()
//                .collect(Collectors.toList());
//
//        Stream<Tuple> subjectStream = entityManager.createQuery("select p.id as painting_id, "
//                        + "sb.id as subject_id, "
//                        + "sb.name as subject_name "
//                        + "from Painting p "
//                        + "join p.subjects sb"
//                        + longsId, Tuple.class)
//                .getResultStream();
//
//        subjectStream.map(tuple -> paintingDtoMap.computeIfPresent(tuple.get("painting_id", Long.class),
//                        (id, dto) -> {
//                            dto.getSubjects().add(new SubjectResponseDto(tuple.get("subject_id", Long.class),
//                                    tuple.get("subject_name", String.class)));
//                            return dto;
//                        }))
//                .distinct()
//                .collect(Collectors.toList());
//
//        List<PaintingDto> paintingDtos = new ArrayList<>(paintingDtoMap.values());
//        int total = paintingDtos.size();
//        return new PageImpl<>(paintingDtos, pageable, total);
        return null;
    }

    @Override
    public Page<PaintingDto> getAllDtosBySpecification(Specification<Painting> specification,
                                                       Pageable pageable) {

        Map<Long, PaintingDto> paintingDtoMap = new LinkedHashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Painting> painting = query.from(Painting.class);
        Join<Painting, Author> author = painting.join(AUTHOR);
        Join<Painting, Image> image = painting.join(IMAGE);

        if (specification != null) {
            Predicate predicate = specification.toPredicate(painting, query, cb);
            query.multiselect(painting.get(ID).alias(PAINTING_ID),
                            painting.get(TITLE).alias(PAINTING_TITLE),
                            painting.get(DESCRIPTION).alias(PAINTING_DESCRIPTION),
                            painting.get(PRICE).alias(PAINTING_PRICE),
                            painting.get(WIDTH).alias(PAINTING_WIDTH),
                            painting.get(HEIGHT).alias(PAINTING_HEIGHT),
                            painting.get(YEAR_OF_CREATION).alias(PAINTING_YEAR_OF_CREATION),
                            author.get(COGNITO_USERNAME).alias(AUTHOR_ID),
                            author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                            image.get(ID).alias(IMAGE_ID),
                            image.get(TRANSFORMED_RATIO).alias(IMAGE_RATIO))
                    .where(predicate)
                    .orderBy(QueryUtils.toOrders(pageable.getSort(), painting, cb));
        } else {
            query.multiselect(painting.get(ID).alias(PAINTING_ID),
                            painting.get(TITLE).alias(PAINTING_TITLE),
                            painting.get(DESCRIPTION).alias(PAINTING_DESCRIPTION),
                            painting.get(PRICE).alias(PAINTING_PRICE),
                            painting.get(WIDTH).alias(PAINTING_WIDTH),
                            painting.get(HEIGHT).alias(PAINTING_HEIGHT),
                            painting.get(YEAR_OF_CREATION).alias(PAINTING_YEAR_OF_CREATION),
                            author.get(COGNITO_USERNAME).alias(AUTHOR_ID),
                            author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                            image.get(ID).alias(IMAGE_ID),
                            image.get(TRANSFORMED_RATIO).alias(IMAGE_RATIO))
                    .orderBy(QueryUtils.toOrders(pageable.getSort(), painting, cb));

        }

        Stream<Tuple> paintingAuthorStream = entityManager.createQuery(query)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((int) pageable.getOffset())
                .getResultStream();



        paintingAuthorStream.map(tuple -> {
                    PaintingDto paintingDto = paintingDtoMap.computeIfAbsent(tuple.get(PAINTING_ID, Long.class),
                            id -> new PaintingDto(tuple.get(PAINTING_ID, Long.class),
                                    tuple.get(PAINTING_TITLE, String.class),
                                    tuple.get(PAINTING_DESCRIPTION, String.class),
                                    tuple.get(PAINTING_PRICE, BigDecimal.class),
                                    tuple.get(PAINTING_WIDTH, Double.class),
                                    tuple.get(PAINTING_HEIGHT, Double.class),
                                    tuple.get(PAINTING_YEAR_OF_CREATION, Integer.class)));

                    paintingDto.setAuthor(new AuthorForPaintingResponseDto(tuple.get(AUTHOR_ID, String.class),
                            tuple.get(AUTHOR_FULL_NAME, String.class)));

                    String url = imageTransformation.generateUrl(tuple.get(IMAGE_ID, String.class),
                            tuple.get(IMAGE_RATIO, Double.class));

                    paintingDto.setImage(new ImageResponse(tuple.get(IMAGE_ID, String.class),
                            tuple.get(IMAGE_RATIO, Double.class), url));

                    return paintingDto;
                })
                .collect(Collectors.toList());


        CriteriaBuilder.In<Object> idPredicate = cb.in(painting.get(ID));
        for (Long id : paintingDtoMap.keySet()) {
            idPredicate.value(id);
        }

        cb = entityManager.getCriteriaBuilder();
        query = cb.createTupleQuery();
        painting = query.from(Painting.class);
        Join<Painting, Style> styles = painting.join(STYLES);
        query.multiselect(painting.get(ID).alias(PAINTING_ID),
                        styles.get(ID).alias(STYLE_ID),
                        styles.get(NAME).alias(STYLE_NAME))
                .where(idPredicate);

        Stream<Tuple> styleStream = entityManager.createQuery(query)
                .getResultStream();

        styleStream.map(tuple ->
                        paintingDtoMap.computeIfPresent(tuple.get(PAINTING_ID, Long.class),
                                (id, dto) -> {
                                    dto.getStyles().add(new StyleResponseDto(tuple.get(STYLE_ID, Long.class),
                                            tuple.get(STYLE_NAME, String.class)));
                                    return dto;
                                }))
                .distinct()
                .collect(Collectors.toList());


        cb = entityManager.getCriteriaBuilder();
        query = cb.createTupleQuery();
        painting = query.from(Painting.class);
        Join<Painting, Medium> mediums = painting.join(MEDIUMS);

        query.multiselect(painting.get(ID).alias(PAINTING_ID),
                        mediums.get(ID).alias(MEDIUM_ID),
                        mediums.get(NAME).alias(MEDIUM_NAME))
                .where(idPredicate);

        Stream<Tuple> mediumStream = entityManager.createQuery(query)
                .getResultStream();

        mediumStream.map(tuple ->
                        paintingDtoMap.computeIfPresent(tuple.get(PAINTING_ID, Long.class),
                                (id, dto) -> {
                                    dto.getMediums().add(new MediumResponseDto(tuple.get(MEDIUM_ID, Long.class),
                                            tuple.get(MEDIUM_NAME, String.class)));
                                    return dto;
                                }))
                .distinct()
                .collect(Collectors.toList());

        cb = entityManager.getCriteriaBuilder();
        query = cb.createTupleQuery();
        painting = query.from(Painting.class);
        Join<Painting, Support> supports = painting.join(SUPPORTS);

        query.multiselect(painting.get(ID).alias(PAINTING_ID),
                        supports.get(ID).alias(SUPPORT_ID),
                        supports.get(NAME).alias(SUPPORT_NAME))
                .where(idPredicate);

        Stream<Tuple> supportStream = entityManager.createQuery(query)
                .getResultStream();

        supportStream.map(tuple ->
                        paintingDtoMap.computeIfPresent(tuple.get(PAINTING_ID, Long.class),
                                (id, dto) -> {
                                    dto.getSupports().add(new SupportResponseDto(tuple.get(SUPPORT_ID, Long.class),
                                            tuple.get(SUPPORT_NAME, String.class)));
                                    return dto;
                                }))
                .distinct()
                .collect(Collectors.toList());

        cb = entityManager.getCriteriaBuilder();
        query = cb.createTupleQuery();
        painting = query.from(Painting.class);
        Join<Painting, Subject> subjects = painting.join(SUBJECTS);

        query.multiselect(painting.get(ID).alias(PAINTING_ID),
                        subjects.get(ID).alias(SUBJECT_ID),
                        subjects.get(NAME).alias(SUBJECT_NAME))
                .where(idPredicate);

        Stream<Tuple> subjectStream = entityManager.createQuery(query)
                .getResultStream();

        subjectStream.map(tuple ->
                        paintingDtoMap.computeIfPresent(tuple.get(PAINTING_ID, Long.class),
                                (id, dto) -> {
                                    dto.getSubjects().add(new SubjectResponseDto(tuple.get(SUBJECT_ID, Long.class),
                                            tuple.get(SUBJECT_NAME, String.class)));
                                    return dto;
                                }))
                .distinct()
                .collect(Collectors.toList());
        List<PaintingDto> paintingDtos = new ArrayList<>(paintingDtoMap.values());

        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Painting> root = countQuery.from(Painting.class);
        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, countQuery, cb);
            countQuery.select(cb.count(root)).where(predicate);
        } else {
            countQuery.select(cb.count(root));
        }
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(paintingDtos, pageable, total);
    }

    @Override
    public Optional<Painting> getByIdWithCustomQuery(Long id) {
        Painting painting = entityManager.createQuery("select distinct p from Painting p "
                                + "join fetch p.author "
                                + "join fetch p.image "
                                + "join fetch p.styles where p.id = ?1",
                        Painting.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter(1, id)
                .getSingleResult();

        painting = entityManager.createQuery("select distinct p from Painting p "
                                + "join fetch p.mediums where p in ?1",
                        Painting.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter(1, painting)
                .getSingleResult();

        painting = entityManager.createQuery("select distinct p from Painting p "
                                + "join fetch p.supports where p in ?1",
                        Painting.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter(1, painting)
                .getSingleResult();

        painting = entityManager.createQuery("select distinct p from Painting p "
                                + "join fetch p.subjects where p in ?1",
                        Painting.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setParameter(1, painting)
                .getSingleResult();

        return Optional.of(painting);
    }
}
