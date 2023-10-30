package com.example.artvswar.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NaturalId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(of = "checkoutSessionId")
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private BigDecimal subtotalAmount;

    private BigDecimal shippingAmount;

    @Column(nullable = false)
    private BigDecimal netAmount;

    private  BigDecimal transferAmount;

    private BigDecimal income;

    @Setter(AccessLevel.PRIVATE)
    @Cascade(CascadeType.SAVE_UPDATE)
    @OneToMany(mappedBy = "order")
    private List<Painting> paintings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String checkoutSessionId;

    @Column(nullable = false, unique = true)
    private String paymentIntentId;

    @Column(unique = true)
    private String chargeId;
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    @Column(columnDefinition = "TINYINT not null default false")
    private boolean isDelivered;

    public void addPainting(Painting painting) {
        paintings.add(painting);
        painting.setOrder(this);
    }
}
