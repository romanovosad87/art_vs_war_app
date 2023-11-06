package com.example.artvswar.repository.order;

import com.example.artvswar.dto.response.order.OrderResponseDto;
import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class CustomOrderRepositoryImpl
        implements CustomOrderRepository {
    private final EntityManager entityManager;

    @Override
    public OrderResponseDto getOrder(Long id) {

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
                        + "from Order o "
                        + "join o.paintings p "
                        + "join p.author a "
                        + "where o.id = ?1", PaintingShortResponseDto.class)
                .setParameter(1, id)
                .getResultList();

        OrderResponseDto responseDto = entityManager.createQuery("select new com.example.artvswar.dto.response.order.OrderResponseDto("
                        + "o.id, o.totalAmount, o.subtotalAmount, o.shippingAmount, o.isDelivered, o.createdAt) "
                        + "from Order o where o.id = ?1", OrderResponseDto.class)
                .setParameter(1, id)
                .getSingleResult();

        responseDto.getPaintings().addAll(paintings);

        return responseDto;
    }

    @Override
    public List<Order> getOrdersCreatedBeforeAndAreNotMarkedAsDelivered(LocalDateTime time) {
        return entityManager.createQuery("select o from Order o "
                        + "where o.isDelivered = false and o.createdAt < ?1", Order.class)
                .setParameter(1, time)
                .getResultList();
    }

    @Override
    public List<Order> getOrdersDeliveredAtBeforeAndDoNotHasTransfer(LocalDateTime time) {
        return entityManager.createQuery("select o from Order o "
                        + "join fetch o.paintings p "
                        + "join fetch p.author a "
                        + "join fetch a.stripeProfile sp "
                        + "where o.isDelivered = true and  o.transferAmount is null "
                        + "and o.deliveredAt < ?1", Order.class)
                .setParameter(1, time)
                .getResultList();

    }
}
