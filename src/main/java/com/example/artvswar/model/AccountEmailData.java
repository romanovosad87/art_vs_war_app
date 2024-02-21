package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
    @Column(columnDefinition = "TINYINT not null default false")
    private boolean unsubscribed;
    @Column(columnDefinition = "TINYINT not null default false")
    private boolean complaint;
    @Column(columnDefinition = "TINYINT not null default false")
    private boolean bounce;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

}
