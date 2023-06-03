package com.example.artvswar.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(of = "cognitoUsername")
@Getter
@Setter
@MappedSuperclass
public abstract class User {
    @Id
    @NaturalId
    @Column(nullable = false, updatable = false, unique = true)
    private String cognitoUsername;
    @Column(nullable = false)
    private String email;
}
