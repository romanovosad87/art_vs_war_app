package com.example.artvswar.repository.painting;

import com.example.artvswar.dto.response.MainPageDataResponseDto;
import com.example.artvswar.dto.response.MediumResponseDto;
import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.dto.response.SubjectResponseDto;
import com.example.artvswar.dto.response.SupportResponseDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.image.ImageResponse;
import com.example.artvswar.dto.response.painting.PaintingDto;
import com.example.artvswar.dto.response.painting.PaintingParametersForSearchResponseDto;
import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.AuthorShippingAddress;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.Medium;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.model.StripeProfile;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.Subject;
import com.example.artvswar.model.Support;
import com.example.artvswar.model.enummodel.ModerationStatus;
import com.example.artvswar.model.enummodel.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomPaintingRepositoryImpl
        implements CustomPaintingRepository {
    private static final String AUTHOR = "author";
    public static final String STRIPE_PROFILE = "stripeProfile";
    public static final String AUTHOR_SHIPPING_ADDRESS = "authorShippingAddress";
    private static final String PAINTING_IMAGE = "paintingImage";
    private static final String IMAGE = "image";
    private static final String STYLES = "styles";
    private static final String MEDIUMS = "mediums";
    private static final String SUPPORTS = "supports";
    private static final String SUBJECTS = "subjects";
    private static final String ID = "id";
    private static final String PUBLIC_ID = "publicId";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DEPTH = "depth";
    private static final String IS_DELETED = "isDeleted";
    private static final String YEAR_OF_CREATION = "yearOfCreation";
    private static final String ADDED_TO_DATABASE = "entityCreatedAt";
    private static final String COGNITO_SUBJECT = "cognitoSubject";
    private static final String PRETTY_ID = "prettyId";
    private static final String FULL_NAME = "fullName";
    private static final String COUNTRY = "country";
    private static final String IMAGE_URL = "imageUrl";
    private static final String URL = "url";
    private static final String MODERATION_STATUS = "moderationStatus";
    private static final String PAYMENT_STATUS = "paymentStatus";
    private static final String TRANSFORMED_RATIO = "transformedRatio";
    private static final String NAME = "name";
    private static final String PAINTING_ID = "painting_id";
    private static final String PAINTING_PRETTY_ID = "painting_pretty_id";
    private static final String PAINTING_TITLE = "painting_title";
    private static final String PAINTING_DESCRIPTION = "painting_description";
    private static final String PAINTING_PRICE = "painting_price";
    private static final String PAINTING_HEIGHT = "painting_height";
    private static final String PAINTING_WIDTH = "painting_width";
    private static final String PAINTING_DEPTH = "painting_depth";
    private static final String PAINTING_YEAR_OF_CREATION = "painting_yearOfCreation";
    private static final String PAINTING_PAYMENT_STATUS = "painting_payment_status";
    private static final String AUTHOR_ID = "author_id";
    private static final String AUTHOR_PRETTY_ID = "author_pretty_id";
    private static final String AUTHOR_FULL_NAME = "author_fullName";
    private static final String AUTHOR_COUNTRY = "author_country";
    private static final String PAINTING_IMAGE_ID = "painting_image_id";
    private static final String PAINTING_IMAGE_MODERATION_STATUS = "painting_moderation_status";
    private static final String IMAGE_RATIO = "image_ratio";
    private static final String PAINTING_IMAGE_URL = "image_url";
    private static final String STYLE_ID = "style_id";
    private static final String STYLE_NAME = "style_name";
    private static final String MEDIUM_ID = "medium_id";
    private static final String MEDIUM_NAME = "medium_name";
    private static final String SUPPORT_ID = "support_id";
    private static final String SUPPORT_NAME = "support_name";
    private static final String SUBJECT_ID = "subject_id";
    private static final String SUBJECT_NAME = "subject_name";
    public static final String DETAILS_SUBMITTED = "isDetailsSubmitted";
    private final EntityManager entityManager;

    @Override
    public Page<PaintingDto> getAllDtosBySpecification(Specification<Painting> specification,
                                                       Pageable pageable) {

        Map<Long, PaintingDto> paintingDtoMap = new LinkedHashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Painting> painting = query.from(Painting.class);
        Join<Painting, Author> author = painting.join(AUTHOR);
        Join<Painting, PaintingImage> image = painting.join(PAINTING_IMAGE);
        Predicate authorIsDeleted = cb.equal(author.get(IS_DELETED), false);

        if (specification != null) {
            Predicate specificationPredicate = specification.toPredicate(painting, query, cb);
            query.multiselect(painting.get(ID).alias(PAINTING_ID),
                            painting.get(PRETTY_ID).alias(PAINTING_PRETTY_ID),
                            painting.get(TITLE).alias(PAINTING_TITLE),
                            painting.get(DESCRIPTION).alias(PAINTING_DESCRIPTION),
                            painting.get(PRICE).alias(PAINTING_PRICE),
                            painting.get(WIDTH).alias(PAINTING_WIDTH),
                            painting.get(HEIGHT).alias(PAINTING_HEIGHT),
                            painting.get(YEAR_OF_CREATION).alias(PAINTING_YEAR_OF_CREATION),
                            author.get(COGNITO_SUBJECT).alias(AUTHOR_ID),
                            author.get(PRETTY_ID).alias(AUTHOR_PRETTY_ID),
                            author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                            author.get(COUNTRY).alias(AUTHOR_COUNTRY),
                            image.get(ID).alias(PAINTING_IMAGE_ID),
                            image.get(IMAGE_URL).alias(PAINTING_IMAGE_URL),
                            image.get(TRANSFORMED_RATIO).alias(IMAGE_RATIO))
                    .where(specificationPredicate, authorIsDeleted)
                    .orderBy(QueryUtils.toOrders(pageable.getSort(), painting, cb));
        } else {
            query.multiselect(painting.get(ID).alias(PAINTING_ID),
                            painting.get(PRETTY_ID).alias(PAINTING_PRETTY_ID),
                            painting.get(TITLE).alias(PAINTING_TITLE),
                            painting.get(DESCRIPTION).alias(PAINTING_DESCRIPTION),
                            painting.get(PRICE).alias(PAINTING_PRICE),
                            painting.get(WIDTH).alias(PAINTING_WIDTH),
                            painting.get(HEIGHT).alias(PAINTING_HEIGHT),
                            painting.get(YEAR_OF_CREATION).alias(PAINTING_YEAR_OF_CREATION),
                            author.get(COGNITO_SUBJECT).alias(AUTHOR_ID),
                            author.get(PRETTY_ID).alias(AUTHOR_PRETTY_ID),
                            author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                            author.get(COUNTRY).alias(AUTHOR_COUNTRY),
                            image.get(ID).alias(PAINTING_IMAGE_ID),
                            image.get(IMAGE_URL).alias(PAINTING_IMAGE_URL),
                            image.get(TRANSFORMED_RATIO).alias(IMAGE_RATIO))
                    .where(authorIsDeleted)
                    .orderBy(QueryUtils.toOrders(pageable.getSort(), painting, cb));

        }

        Stream<Tuple> paintingAuthorStream = entityManager.createQuery(query)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((int) pageable.getOffset())
                .getResultStream();


        paintingAuthorStream.map(tuple -> {
                    PaintingDto paintingDto = paintingDtoMap.computeIfAbsent(tuple.get(PAINTING_ID, Long.class),
                            id -> new PaintingDto(tuple.get(PAINTING_ID, Long.class),
                                    tuple.get(PAINTING_PRETTY_ID, String.class),
                                    tuple.get(PAINTING_TITLE, String.class),
                                    tuple.get(PAINTING_DESCRIPTION, String.class),
                                    tuple.get(PAINTING_PRICE, BigDecimal.class),
                                    tuple.get(PAINTING_WIDTH, Double.class),
                                    tuple.get(PAINTING_HEIGHT, Double.class),
                                    tuple.get(PAINTING_YEAR_OF_CREATION, Integer.class)));

                    paintingDto.setAuthor(new AuthorForPaintingResponseDto(tuple.get(AUTHOR_ID, String.class),
                            tuple.get(AUTHOR_PRETTY_ID, String.class),
                            tuple.get(AUTHOR_FULL_NAME, String.class),
                            tuple.get(AUTHOR_COUNTRY, String.class)));


                    paintingDto.setImage(new ImageResponse(tuple.get(PAINTING_IMAGE_ID, String.class),
                            tuple.get(IMAGE_RATIO, Double.class), tuple.get(PAINTING_IMAGE_URL, String.class)));

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
            countQuery.select(root.get("id")).where(predicate);
        } else {
            countQuery.select(root.get("id"));
        }
        long total = entityManager.createQuery(countQuery).getResultStream().distinct().count();

        return new PageImpl<>(paintingDtos, pageable, total);
    }

    @Override
    public Page<PaintingShortResponseDto> getAllShortDtosBySpecification(
            Specification<Painting> specification,
            Pageable pageable) {

        Map<Long, PaintingShortResponseDto> paintingDtoMap = new LinkedHashMap<>();

        Sort sort = pageable.getSort();
        if (sort == Sort.unsorted()) {
            sort = Sort.by(Sort.Direction.DESC, ADDED_TO_DATABASE);
        } else {
            if (pageable.getSort().get().anyMatch(order -> order.getProperty().equals(PRICE))) {
                sort = pageable.getSort().and(Sort.by(Sort.Direction.DESC, ID));
            }
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Painting> painting = query.from(Painting.class);
        Join<Painting, Author> author = painting.join(AUTHOR);
        Join<Author, StripeProfile> stripeProfile = author.join(STRIPE_PROFILE);
        Join<Author, AuthorShippingAddress> authorShippingAddress = author.join(AUTHOR_SHIPPING_ADDRESS);
        Join<Painting, PaintingImage> paintingImage = painting.join(PAINTING_IMAGE);
        Join<PaintingImage, Image> image = paintingImage.join(IMAGE);
        Predicate authorIsDeleted = cb.equal(author.get(IS_DELETED), false);
        Predicate moderationStatus = cb.equal(image.get(MODERATION_STATUS), ModerationStatus.APPROVED);
        Predicate stripeProfileSubmitted = cb.equal(stripeProfile.get(DETAILS_SUBMITTED), true);
        Predicate isAddress = cb.isNotNull(authorShippingAddress.get(ID));

        if (specification != null) {
            Predicate specificationPredicate = specification.toPredicate(painting, query, cb);
            query.multiselect(painting.get(ID).alias(PAINTING_ID),
                    painting.get(PRETTY_ID).alias(PAINTING_PRETTY_ID),
                    painting.get(TITLE).alias(PAINTING_TITLE),
                    painting.get(PRICE).alias(PAINTING_PRICE),
                    painting.get(WIDTH).alias(PAINTING_WIDTH),
                    painting.get(HEIGHT).alias(PAINTING_HEIGHT),
                    painting.get(DEPTH).alias(PAINTING_DEPTH),
                    painting.get(YEAR_OF_CREATION).alias(PAINTING_YEAR_OF_CREATION),
                    painting.get(PAYMENT_STATUS).alias(PAINTING_PAYMENT_STATUS),
                    author.get(PRETTY_ID).alias(AUTHOR_PRETTY_ID),
                    author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                    author.get(COUNTRY).alias(AUTHOR_COUNTRY),
                    image.get(PUBLIC_ID).alias(PAINTING_IMAGE_ID),
                    image.get(URL).alias(PAINTING_IMAGE_URL),
                    image.get(MODERATION_STATUS).alias(PAINTING_IMAGE_MODERATION_STATUS))
                            .where(specificationPredicate, moderationStatus, authorIsDeleted,
                                    stripeProfileSubmitted, isAddress)
                            .orderBy(QueryUtils.toOrders(sort, painting, cb))
                            .distinct(true);

        } else {
            query.multiselect(painting.get(ID).alias(PAINTING_ID),
                            painting.get(PRETTY_ID).alias(PAINTING_PRETTY_ID),
                            painting.get(TITLE).alias(PAINTING_TITLE),
                            painting.get(PRICE).alias(PAINTING_PRICE),
                            painting.get(WIDTH).alias(PAINTING_WIDTH),
                            painting.get(HEIGHT).alias(PAINTING_HEIGHT),
                            painting.get(DEPTH).alias(PAINTING_DEPTH),
                            painting.get(YEAR_OF_CREATION).alias(PAINTING_YEAR_OF_CREATION),
                            painting.get(PAYMENT_STATUS).alias(PAINTING_PAYMENT_STATUS),
                            author.get(PRETTY_ID).alias(AUTHOR_PRETTY_ID),
                            author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                            author.get(COUNTRY).alias(AUTHOR_COUNTRY),
                            image.get(PUBLIC_ID).alias(PAINTING_IMAGE_ID),
                            image.get(URL).alias(PAINTING_IMAGE_URL),
                            image.get(MODERATION_STATUS).alias(PAINTING_IMAGE_MODERATION_STATUS))
                    .where(moderationStatus, authorIsDeleted, stripeProfileSubmitted, isAddress)
                    .orderBy(QueryUtils.toOrders(sort, painting, cb));
        }

        Stream<Tuple> paintingAuthorStream = entityManager.createQuery(query)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((int) pageable.getOffset())
                .getResultStream();


        List<PaintingShortResponseDto> dtos = paintingAuthorStream.map(tuple -> paintingDtoMap.computeIfAbsent(tuple.get(PAINTING_ID, Long.class),
                        id -> new PaintingShortResponseDto(tuple.get(PAINTING_ID, Long.class),
                                tuple.get(PAINTING_PRETTY_ID, String.class),
                                tuple.get(PAINTING_TITLE, String.class),
                                tuple.get(PAINTING_PRICE, BigDecimal.class),
                                tuple.get(PAINTING_WIDTH, Double.class),
                                tuple.get(PAINTING_HEIGHT, Double.class),
                                tuple.get(PAINTING_DEPTH, Double.class),
                                tuple.get(PAINTING_YEAR_OF_CREATION, Integer.class),
                                tuple.get(PAINTING_PAYMENT_STATUS, PaymentStatus.class),
                                tuple.get(PAINTING_IMAGE_ID, String.class),
                                tuple.get(PAINTING_IMAGE_URL, String.class),
                                tuple.get(PAINTING_IMAGE_MODERATION_STATUS, ModerationStatus.class),
                                tuple.get(AUTHOR_FULL_NAME, String.class),
                                tuple.get(AUTHOR_PRETTY_ID, String.class),
                                tuple.get(AUTHOR_COUNTRY, String.class))))
                .collect(Collectors.toList());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Painting> root = countQuery.from(Painting.class);
        author = root.join(AUTHOR);
        stripeProfile = author.join(STRIPE_PROFILE);
        authorShippingAddress = author.join(AUTHOR_SHIPPING_ADDRESS);
        paintingImage = root.join(PAINTING_IMAGE);
        image = paintingImage.join(IMAGE);
        authorIsDeleted = cb.equal(author.get(IS_DELETED), false);
        moderationStatus = cb.equal(image.get(MODERATION_STATUS), ModerationStatus.APPROVED);
        stripeProfileSubmitted = cb.equal(stripeProfile.get(DETAILS_SUBMITTED), true);
        isAddress = cb.isNotNull(authorShippingAddress.get(ID));

        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, countQuery, cb);
            countQuery.select(root.get(ID)).where(predicate, authorIsDeleted, moderationStatus,
                            stripeProfileSubmitted, isAddress)
                    .distinct(true);
        } else {
            countQuery.select(root.get(ID)).where(authorIsDeleted, moderationStatus,
                    stripeProfileSubmitted, isAddress);
        }
        long total = entityManager.createQuery(countQuery).getResultStream().count();

        return new PageImpl<>(dtos, pageable, total);
    }

    @Override
    public Page<PaintingShortResponseDto> findAllByAuthorPrettyId(String authorPrettyId,
                                                                  Pageable pageable) {
        List<PaintingShortResponseDto> resultList = entityManager.createQuery("select new com.example.artvswar.dto.response.painting.PaintingShortResponseDto("
                                + "p.id, "
                                + "p.prettyId, "
                                + "p.title, "
                                + "p.price, "
                                + "p.width, "
                                + "p.height, "
                                + "p.depth, "
                                + "p.yearOfCreation, "
                                + "p.paymentStatus, "
                                + "p.paintingImage.image.publicId, "
                                + "p.paintingImage.image.url, "
                                + "p.paintingImage.image.moderationStatus, "
                                + "p.author.fullName,"
                                + "p.author.prettyId, "
                                + "p.author.country) "
                                + "from Painting p "
                                + "inner join p.author a "
                                + "where p.paymentStatus != 10 and a.prettyId =?1 "
                                + "and p.paintingImage.image.moderationStatus = 20 "
                                + "order by p.entityCreatedAt desc",
                        PaintingShortResponseDto.class)
                .setParameter(1, authorPrettyId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = entityManager.createQuery("select count(p.id) from Painting p "
                        + "where p.paymentStatus != 10 and p.author.prettyId =?1 "
                        + "and p.paintingImage.image.moderationStatus = 20", Long.class)
                .setParameter(1, authorPrettyId)
                .getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public List<PaintingShortResponseDto> getAdditionalPaintings(String paintingPrettyId,
                                                                 String authorPrettyId,
                                                                 int limit) {
        return entityManager.createQuery("select new com.example.artvswar.dto.response.painting.PaintingShortResponseDto("
                        + "p.id, "
                        + "p.prettyId, "
                        + "p.title, "
                        + "p.price, "
                        + "p.width, "
                        + "p.height, "
                        + "p.depth, "
                        + "p.yearOfCreation, "
                        + "p.paymentStatus, "
                        + "p.paintingImage.image.publicId, "
                        + "p.paintingImage.image.url, "
                        + "p.paintingImage.image.moderationStatus, "
                        + "p.author.fullName,"
                        + "p.author.prettyId, "
                        + "p.author.country) "
                        + "from Painting p "
                        + "inner join p.author a "
                        + "where a.prettyId =?1 and p.prettyId !=?2 and p.paymentStatus = 0 "
                        + "and p.paintingImage.image.moderationStatus = 20"
                        + "order by rand()", PaintingShortResponseDto.class)
                .setParameter(1, authorPrettyId)
                .setParameter(2, paintingPrettyId)
                .setFirstResult(0)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<PaintingShortResponseDto> getRecommendedPaintings(Set<String> paintingPrettyIds,
                                                                  int limit) {

        List<Long> subjectIds = entityManager.createQuery("select distinct sub.id from Painting p join p.subjects sub where p.prettyId in (?1)", Long.class)
                .setParameter(1, paintingPrettyIds)
                .getResultList();

        List<Long> stylesIds = entityManager.createQuery("select distinct st.id from Painting p join p.styles st where p.prettyId in (?1)", Long.class)
                .setParameter(1, paintingPrettyIds)
                .getResultList();

        return entityManager.createQuery("select distinct new com.example.artvswar.dto.response.painting.PaintingShortResponseDto("
                        + "p.id, "
                        + "p.prettyId, "
                        + "p.title, "
                        + "p.price, "
                        + "p.width, "
                        + "p.height, "
                        + "p.depth, "
                        + "p.yearOfCreation, "
                        + "p.paymentStatus, "
                        + "p.paintingImage.image.publicId, "
                        + "p.paintingImage.image.url, "
                        + "p.paintingImage.image.moderationStatus, "
                        + "p.author.fullName,"
                        + "p.author.prettyId, "
                        + "p.author.country) "
                        + "from Painting p "
                        + "inner join p.author a "
                        + "inner join p.subjects sub "
                        + "inner join p.styles st "
                        + "where p.paymentStatus = 0 and a.isDeleted = false "
                        + "and a.authorShippingAddress.id != null and a.stripeProfile.isDetailsSubmitted = true "
                        + "and p.paintingImage.image.moderationStatus = 20 "
                        + "and p.prettyId not in (?1) and (sub.id in (?2) or st.id in (?3)) "
                        + "order by rand()", PaintingShortResponseDto.class)
                .setParameter(1, paintingPrettyIds)
                .setParameter(2, subjectIds)
                .setParameter(3, stylesIds)
                .setFirstResult(0)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public PaintingParametersForSearchResponseDto getDistinctParameters() {

        PaintingParametersForSearchResponseDto dto = entityManager
                .createQuery("select new com.example.artvswar.dto.response.painting.PaintingParametersForSearchResponseDto("
                        + "min(p.price), max(p.price), "
                        + "min(p.width), max(p.width), "
                        + "min (p.height), max(p.height)) "
                        + "from Painting p "
                        + "join p.author a "
                        + "where p.paymentStatus != 10 and a.isDeleted = false "
                        + "and a.authorShippingAddress.id != null and a.stripeProfile.isDetailsSubmitted = true "
                        + "and p.paintingImage.image.moderationStatus = 20", PaintingParametersForSearchResponseDto.class)
                .getSingleResult();

        dto.setStyles(entityManager
                .createQuery("select distinct st.name from Painting p "
                        + "join p.styles st "
                        + "join p.author a "
                        + "where p.paymentStatus != 10 and a.isDeleted = false "
                        + "and a.authorShippingAddress.id != null and a.stripeProfile.isDetailsSubmitted = true "
                        + "and p.paintingImage.image.moderationStatus = 20", String.class)
                .getResultList());

        dto.setMediums(entityManager
                .createQuery("select distinct md.name from Painting p "
                        + "join p.mediums md "
                        + "join p.author a "
                        + "where p.paymentStatus != 10 and a.isDeleted = false "
                        + "and a.authorShippingAddress.id != null and a.stripeProfile.isDetailsSubmitted = true "
                        + "and p.paintingImage.image.moderationStatus = 20", String.class)
                .getResultList());

        dto.setSupports(entityManager
                .createQuery("select distinct sp.name from Painting p "
                        + "join p.supports sp "
                        + "join p.author a "
                        + "where p.paymentStatus != 10 and a.isDeleted = false "
                        + "and a.authorShippingAddress.id != null and a.stripeProfile.isDetailsSubmitted = true "
                        + "and p.paintingImage.image.moderationStatus = 20", String.class)
                .getResultList());

        dto.setSubjects(entityManager
                .createQuery("select distinct sb.name from Painting p "
                        + "join p.subjects sb "
                        + "join p.author a "
                        + "where p.paymentStatus != 10 and a.isDeleted = false "
                        + "and a.authorShippingAddress.id != null and a.stripeProfile.isDetailsSubmitted = true "
                        + "and p.paintingImage.image.moderationStatus = 20", String.class)
                .getResultList());

        return dto;
    }

    @Override
    public Page<PaintingShortResponseDto> findAllByCollectionPrettyId(String collectionPrettyId,
                                                                      Pageable pageable) {
        List<PaintingShortResponseDto> resultList = entityManager.createQuery("select new com.example.artvswar.dto.response.painting.PaintingShortResponseDto("
                                + "p.id, "
                                + "p.prettyId, "
                                + "p.title, "
                                + "p.price, "
                                + "p.width, "
                                + "p.height, "
                                + "p.depth, "
                                + "p.yearOfCreation, "
                                + "p.paymentStatus, "
                                + "p.paintingImage.image.publicId, "
                                + "p.paintingImage.image.url, "
                                + "p.paintingImage.image.moderationStatus, "
                                + "p.author.fullName,"
                                + "p.author.prettyId, "
                                + "p.author.country) "
                                + "from Painting p "
                                + "inner join p.collection c "
                                + "where p.paymentStatus != 10 and c.prettyId =?1 "
                                + "and p.paintingImage.image.moderationStatus = 20 "
                                + "order by p.entityCreatedAt desc",
                        PaintingShortResponseDto.class)
                .setParameter(1, collectionPrettyId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = entityManager.createQuery("select count(p.id) from Painting p "
                        + "where p.paymentStatus != 10 and p.collection.prettyId =?1 "
                        + "and p.paintingImage.image.moderationStatus = 20", Long.class)
                .setParameter(1, collectionPrettyId)
                .getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public MainPageDataResponseDto getDataForMainPage() {
        Long paintingsQuantity = entityManager.createQuery("select count(p.id) "
                + "from Painting p "
                + "where p.paintingImage.image.moderationStatus = 20 "
                + "and p.author.isDeleted = false "
                + "and p.author.stripeProfile.isDetailsSubmitted = true "
                + "and p.author.authorShippingAddress.id != null", Long.class).getSingleResult();
        paintingsQuantity = paintingsQuantity == null ? 0L : paintingsQuantity;

        Long authorsQuantity = entityManager.createQuery("select count(a.id) "
                        + "from Author a "
                        + "where a.isDeleted = false "
                        + "and a.authorPhoto.image.moderationStatus = 20 "
                        + "and a.stripeProfile.isDetailsSubmitted = true "
                        + "and a.authorShippingAddress.id != null",
                Long.class).getSingleResult();
        authorsQuantity = authorsQuantity == null ? 0L : authorsQuantity;

        BigDecimal sumOfSoldPaintings = entityManager.createQuery("select sum(p.price) "
                        + "from Painting p where p.paymentStatus = 20",
                BigDecimal.class).getSingleResult();
        sumOfSoldPaintings = sumOfSoldPaintings == null ? BigDecimal.ZERO : sumOfSoldPaintings;

        return new MainPageDataResponseDto(authorsQuantity, paintingsQuantity, sumOfSoldPaintings);

    }

    @Override
    public Page<PaintingShortResponseDto> findResentSelling(String authorSubject,
                                                            Pageable pageable) {
        List<PaintingShortResponseDto> resultList = entityManager.createQuery("select new com.example.artvswar.dto.response.painting.PaintingShortResponseDto("
                                + "p.id, "
                                + "p.prettyId, "
                                + "p.title, "
                                + "p.price, "
                                + "p.width, "
                                + "p.height, "
                                + "p.depth, "
                                + "p.yearOfCreation, "
                                + "p.paymentStatus, "
                                + "p.paintingImage.image.publicId, "
                                + "p.paintingImage.image.url, "
                                + "p.paintingImage.image.moderationStatus, "
                                + "p.author.fullName,"
                                + "p.author.prettyId, "
                                + "p.author.country) "
                                + "from Painting p "
                                + "inner join p.author a "
                                + "where p.paymentStatus = 20 and a.cognitoSubject =?1 "
                                + "order by p.updatedAt desc",
                        PaintingShortResponseDto.class)
                .setParameter(1, authorSubject)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = entityManager.createQuery("select count(p.id) from Painting p "
                        + "where p.paymentStatus = 20 and p.author.cognitoSubject =?1", Long.class)
                .setParameter(1, authorSubject)
                .getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}
