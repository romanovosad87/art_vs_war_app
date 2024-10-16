package com.example.artvswar.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import jakarta.persistence.Column;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ShoppingCartPaintingKey implements Serializable {
    @Column(name = "painting_id")
    private Long paintingId;
    @Column(name = "shopping_cart_id")
    private Long shoppingCartId;
}
