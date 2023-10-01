package com.example.artvswar.repository.shoppingCart;

import com.example.artvswar.dto.response.shoppingCart.ShoppingCartResponseDto;
import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class CustomShoppingCartRepositoryImpl
        implements CustomShoppingCartRepository {
    private final EntityManager entityManager;

    @Override
    public ShoppingCartResponseDto get(String cognitoSubject) {

        List<PaintingShortResponseDto> paintings = entityManager.createQuery("select new com.example.artvswar.dto.response.painting.PaintingShortResponseDto("
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
                        + "a.fullName, "
                        + "a.prettyId, "
                        + "a.country) "
                        + "from ShoppingCart sc "
                        + "join sc.paintings scp "
                        + "join scp.painting p "
                        + "join p.author a "
                        + "join sc.account ac "
                        + "where ac.cognitoSubject = ?1 "
                        + "order by scp.createdAt asc", PaintingShortResponseDto.class)
                .setParameter(1, cognitoSubject)
                .getResultList();

        BigDecimal total = paintings.stream()
                .map(PaintingShortResponseDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ShoppingCartResponseDto responseDto = new ShoppingCartResponseDto();
        responseDto.getPaintings().addAll(paintings);

        responseDto.setTotal(total);

        return responseDto;
    }
}
