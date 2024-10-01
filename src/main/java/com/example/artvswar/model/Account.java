package com.example.artvswar.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "cognitoSubject")
@Where(clause = "is_deleted = false")
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    @Column(nullable = false, updatable = false, unique = true)
    private String cognitoSubject;

    @Column(nullable = false, updatable = false)
    private String cognitoUsername;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;
    @Column(unique = true)
    private String stripeCustomerId;

    @ElementCollection
    @Setter(AccessLevel.PRIVATE)
    @CollectionTable(name = "account_shipping_addresses", joinColumns = @JoinColumn(name = "account_id"))
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<AccountShippingAddress> shippingAddresses = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "account")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    @Cascade(value = {CascadeType.PERSIST})
    private AccountEmailData accountEmailData;

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

    public void addAccountEmailData(AccountEmailData data) {
        this.setAccountEmailData(data);
        data.setAccount(this);
    }

    @Override
    public String toString() {
        return "Account {"
                + "id=" + id
                + ", cognitoSubject='" + cognitoSubject + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", phone='" + phone + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
