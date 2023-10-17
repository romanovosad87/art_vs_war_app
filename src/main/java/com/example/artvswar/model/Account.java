package com.example.artvswar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "cognitoSubject")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(nullable = false, updatable = false)
    private String cognitoSubject;

    @Column(nullable = false, updatable = false)
    private String cognitoUsername;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;
    @Column(unique = true)
    private String stripeCustomerId;

    @ElementCollection
    @Setter(AccessLevel.PRIVATE)
    @CollectionTable(name = "account_shipping_addresses", joinColumns = @JoinColumn(name = "account_id"))
    @Cascade(CascadeType.ALL)
    private List<AccountShippingAddress> shippingAddresses = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "TINYINT default false")
    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "INT default 0")
    private int offset;

    public void addOrder(Order order) {
        orders.add(order);
        order.setAccount(this);
    }
}
