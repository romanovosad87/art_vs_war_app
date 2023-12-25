package com.example.artvswar.repository.author;

import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.dto.response.author.AuthorCheckStripeAndAddressPresenceResponseDto;
import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.dto.response.author.AuthorsAllStylesDto;
import com.example.artvswar.dto.response.painting.PaintingForAllAuthorsDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.AuthorPhoto;
import com.example.artvswar.model.AuthorShippingAddress;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.StripeProfile;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.enumModel.ModerationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class CustomAuthorRepositoryImpl
        implements CustomAuthorRepository {
    private static final String COGNITO_SUBJECT = "cognitoSubject";
    private static final String ID = "id";
    private static final String PUBLIC_ID = "publicId";
    private static final String URL = "url";
    private static final String MODERATION_STATUS = "moderationStatus";
    private static final String PAINTINGS = "paintings";
    private static final String PHOTO = "authorPhoto";
    private static final String IMAGE = "image";
    private static final String PRETTY_ID = "prettyId";
    private static final String FULL_NAME = "fullName";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String ABOUT_ME = "aboutMe";
    private static final String STYLES = "styles";
    private static final String NAME = "name";
    private static final String AUTHOR_COGNITO_SUBJECT = "author_cognito_subject";
    private static final String AUTHOR_PRETTY_ID = "author_prettyId";
    private static final String AUTHOR_FULL_NAME = "author_fullName";
    private static final String AUTHOR_COUNTRY = "author_country";
    private static final String AUTHOR_CITY = "author_city";
    private static final String AUTHOR_ABOUT_ME = "author_aboutMe";
    private static final String PAINTING_ID = "painting_id";
    private static final String STYLE_ID = "style_id";
    private static final String STYLE_NAME = "style_name";
    private static final String IMAGE_PUBLIC_ID = "image_publicId";
    private static final String IMAGE_URL = "image_url";
    private static final String IS_DELETED = "isDeleted";
    public static final String STRIPE_PROFILE = "stripeProfile";
    public static final String DETAILS_SUBMITTED = "isDetailsSubmitted";
    public static final String AUTHOR_SHIPPING_ADDRESS = "authorShippingAddress";
    private final EntityManager entityManager;

    @Override
    public Map<String, Set<String>> authorsAllStyleMap(List<String> authorsCognitoSubjects) {
        Map<String, AuthorsAllStylesDto> allAuthorsDtoMap = new LinkedHashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Author> author = query.from(Author.class);
        Join<Author, Painting> paintings = author.join(PAINTINGS);

        CriteriaBuilder.In<String> idPredicate = cb.in(author.get(COGNITO_SUBJECT));
        for (String id : authorsCognitoSubjects) {
            idPredicate.value(id);
        }

        query.multiselect(author.get(COGNITO_SUBJECT).alias(AUTHOR_COGNITO_SUBJECT),
                        paintings.get(ID).alias(PAINTING_ID))
                .where(idPredicate);

        Stream<Tuple> paintingStream = entityManager.createQuery(query)
                .getResultStream();

        paintingStream.map(tuple -> {
                    AuthorsAllStylesDto authorsAllStylesDto = allAuthorsDtoMap.computeIfAbsent(tuple.get(AUTHOR_COGNITO_SUBJECT, String.class),
                            id -> new AuthorsAllStylesDto(tuple.get(AUTHOR_COGNITO_SUBJECT, String.class)));
                    Map<Long, PaintingForAllAuthorsDto> longPaintingsMap = authorsAllStylesDto.getLongPaintingsMap();
                                longPaintingsMap.computeIfAbsent(tuple.get(PAINTING_ID, Long.class),
                                        paintingId -> new PaintingForAllAuthorsDto(tuple.get(PAINTING_ID, Long.class)));
                                return authorsAllStylesDto;
                })
                .distinct()
                .collect(Collectors.toList());


        cb = entityManager.getCriteriaBuilder();
        query = cb.createTupleQuery();
        author = query.from(Author.class);
        paintings = author.join(PAINTINGS);
        Join<Painting, Style> styles = paintings.join(STYLES);
        query.multiselect(author.get(COGNITO_SUBJECT).alias(AUTHOR_COGNITO_SUBJECT),
                        paintings.get(ID).alias(PAINTING_ID),
                        styles.get(ID).alias(STYLE_ID),
                        styles.get(NAME).alias(STYLE_NAME))
                .where(idPredicate);

        Stream<Tuple> styleStream = entityManager.createQuery(query)
                .getResultStream();

        styleStream.map(tuple ->
                        allAuthorsDtoMap.computeIfPresent(tuple.get(AUTHOR_COGNITO_SUBJECT, String.class),
                                (id, dto) -> {
                                    Map<Long, PaintingForAllAuthorsDto> longPaintingsMap = dto.getLongPaintingsMap();
                                    longPaintingsMap.computeIfPresent(tuple.get(PAINTING_ID, Long.class),
                                            (paintingId, paintingDto) -> {
                                                paintingDto.getStyles().add(new StyleResponseDto(tuple.get(STYLE_ID, Long.class),
                                                        tuple.get(STYLE_NAME, String.class)));
                                                return paintingDto;
                                            });
                                    Set<String> stylesName = dto.getStyles();
                                    dto.getLongPaintingsMap().values()
                                            .stream()
                                            .flatMap(paintingDto -> paintingDto.getStyles().stream())
                                            .map(StyleResponseDto::getName)
                                            .forEach(stylesName::add);
                                    return dto;
                                }))
                .distinct()
                .collect(Collectors.toList());

        return allAuthorsDtoMap.values().stream()
                .collect(Collectors.toMap(AuthorsAllStylesDto::getCognitoSubject,
                        AuthorsAllStylesDto::getStyles));
    }

    @Override
    public Page<AuthorResponseDto> findAllBySpecification(Specification<Author> specification,
                                                          Pageable pageable) {
        Map<String, AuthorResponseDto> authorDtoMap = new LinkedHashMap<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Author> author = query.from(Author.class);
        Join<Author, StripeProfile> stripeProfile = author.join(STRIPE_PROFILE);
        Join<Author, AuthorShippingAddress> authorShippingAddress = author.join(AUTHOR_SHIPPING_ADDRESS);
        Join<Author, AuthorPhoto> authorPhoto = author.join(PHOTO);
        Join<AuthorPhoto, Image> image = authorPhoto.join(IMAGE);
        Predicate moderationStatus = cb.equal(image.get(MODERATION_STATUS), ModerationStatus.APPROVED);
        Predicate authorIsDeleted = cb.equal(author.get(IS_DELETED), false);
        Predicate stripeProfileSubmitted = cb.equal(stripeProfile.get(DETAILS_SUBMITTED), true);
        Predicate isAddress = cb.isNotNull(authorShippingAddress.get(ID));

        if (specification != null) {
            Predicate specificationPredicate = specification.toPredicate(author, query, cb);
            query.multiselect(author.get(COGNITO_SUBJECT).alias(AUTHOR_COGNITO_SUBJECT),
                            author.get(PRETTY_ID).alias(AUTHOR_PRETTY_ID),
                            author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                            author.get(COUNTRY).alias(AUTHOR_COUNTRY),
                            author.get(CITY).alias(AUTHOR_CITY),
                            author.get(ABOUT_ME).alias(AUTHOR_ABOUT_ME),
                            image.get(PUBLIC_ID).alias(IMAGE_PUBLIC_ID),
                            image.get(URL).alias(IMAGE_URL))
                    .where(specificationPredicate, authorIsDeleted, moderationStatus, stripeProfileSubmitted, isAddress)
                    .distinct(true)
                    .orderBy(QueryUtils.toOrders(pageable.getSort(), author, cb));
        } else {
            query.multiselect(author.get(COGNITO_SUBJECT).alias(AUTHOR_COGNITO_SUBJECT),
                            author.get(PRETTY_ID).alias(AUTHOR_PRETTY_ID),
                            author.get(FULL_NAME).alias(AUTHOR_FULL_NAME),
                            author.get(COUNTRY).alias(AUTHOR_COUNTRY),
                            author.get(CITY).alias(AUTHOR_CITY),
                            author.get(ABOUT_ME).alias(AUTHOR_ABOUT_ME),
                            image.get(PUBLIC_ID).alias(IMAGE_PUBLIC_ID),
                            image.get(URL).alias(IMAGE_URL))
                    .where(authorIsDeleted, moderationStatus, stripeProfileSubmitted, isAddress)
                    .orderBy(QueryUtils.toOrders(pageable.getSort(), author, cb));
        }

        Stream<Tuple> authorStream = entityManager.createQuery(query)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult((int) pageable.getOffset())
                .getResultStream();

        List<AuthorResponseDto> dtos = authorStream.map(tuple -> authorDtoMap.computeIfAbsent(tuple.get(AUTHOR_COGNITO_SUBJECT, String.class),
                        id -> new AuthorResponseDto(tuple.get(AUTHOR_COGNITO_SUBJECT, String.class),
                                tuple.get(AUTHOR_PRETTY_ID, String.class),
                                tuple.get(AUTHOR_FULL_NAME, String.class),
                                tuple.get(AUTHOR_COUNTRY, String.class),
                                tuple.get(AUTHOR_CITY, String.class),
                                tuple.get(AUTHOR_ABOUT_ME, String.class),
                                tuple.get(IMAGE_PUBLIC_ID, String.class),
                                tuple.get(IMAGE_URL, String.class))))
                .collect(Collectors.toList());

        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Author> root = countQuery.from(Author.class);
        authorShippingAddress = root.join(AUTHOR_SHIPPING_ADDRESS);
        stripeProfile = root.join(STRIPE_PROFILE);
        authorPhoto = root.join(PHOTO);
        image = authorPhoto.join(IMAGE);
        moderationStatus = cb.equal(image.get(MODERATION_STATUS), ModerationStatus.APPROVED);
        authorIsDeleted = cb.equal(root.get(IS_DELETED), false);
        stripeProfileSubmitted = cb.equal(stripeProfile.get(DETAILS_SUBMITTED), true);
        isAddress = cb.isNotNull(authorShippingAddress.get(ID));
        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, countQuery, cb);
            countQuery.select(root.get(ID))
                    .where(predicate, authorIsDeleted, moderationStatus, stripeProfileSubmitted, isAddress)
                    .distinct(true);
        } else {
            countQuery.select(root.get(ID))
                    .where(authorIsDeleted, moderationStatus, stripeProfileSubmitted, isAddress);
        }
        long total = entityManager.createQuery(countQuery).getResultStream().count();

        return new PageImpl<>(dtos, pageable, total);

    }

    @Override
    public AuthorCheckStripeAndAddressPresenceResponseDto checkAuthorProfile(
            String cognitoSubject) {
        AuthorCheckStripeAndAddressPresenceResponseDto dto;
        var isStripeAccountQuery  = entityManager.createQuery("select new com.example.artvswar.dto.response.author.AuthorCheckStripeAndAddressPresenceResponseDto("
                        + "sp.isDetailsSubmitted) from Author a join a.stripeProfile sp "
                        + "where a.cognitoSubject = ?1", AuthorCheckStripeAndAddressPresenceResponseDto.class)
                .setParameter(1, cognitoSubject);
        try {
            dto = isStripeAccountQuery.getSingleResult();
        } catch (NoResultException e) {
            dto = new AuthorCheckStripeAndAddressPresenceResponseDto(false);
        }

        var isShippingAddressQuery = entityManager.createQuery("select case when (asa.id is null) then FALSE else TRUE end "
                        + "from AuthorShippingAddress asa join asa.author a where a.cognitoSubject = ?1", Boolean.class)
                .setParameter(1, cognitoSubject);
        boolean result = false;
        try {
             result = isShippingAddressQuery.getSingleResult();
        } catch (NoResultException e) {
            dto.setHasAddress(false);
        }
        dto.setHasAddress(result);
        return dto;
    }
}
