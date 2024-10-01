package com.example.artvswar.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE admins SET is_deleted = true where id = ?",
        check = ResultCheckStyle.COUNT)
@Table(name = "admins")
public class Admin extends User {
    @Column(nullable = false)
    private String fullName;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;
}
