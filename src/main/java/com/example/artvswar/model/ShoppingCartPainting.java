package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@IdClass(ShoppingCartPaintingKey.class)
@Table(name = "shopping_carts_paintings")
public class ShoppingCartPainting {
    @Id
    private Long paintingId;

    @Id
    private Long shoppingCartId;

    @ManyToOne
    @MapsId("paintingId")
    @JoinColumn(name = "painting_id")
    private Painting painting;

    @ManyToOne
    @MapsId("shoppingCartId")
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
