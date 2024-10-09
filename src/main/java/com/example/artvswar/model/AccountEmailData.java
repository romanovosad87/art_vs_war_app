package com.example.artvswar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "account_email_datas", indexes = {
        @Index(name = "IDX_email", columnList = "email")
})
public class AccountEmailData {
    @Id
    Long id;
    @Column(nullable = false)
    private String email;
    @Column(columnDefinition = "TINYINT(1) not null default false")
    private boolean unsubscribed;
    @Column(columnDefinition = "TINYINT(1) not null default false")
    private boolean complaint;
    @Column(columnDefinition = "TINYINT(1) not null default false")
    private boolean bounce;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

}
