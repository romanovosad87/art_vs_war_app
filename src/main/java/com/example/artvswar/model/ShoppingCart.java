package com.example.artvswar.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;

    @OneToMany(mappedBy = "shoppingCart")
    Set<ShoppingCartPainting> paintings = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;
}
