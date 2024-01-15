package com.example.artvswar.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@EqualsAndHashCode(of = "cognitoSubject")
@Getter
@Setter
@MappedSuperclass
public abstract class User {

    @Id
    private Long id;

    @NaturalId
    @Column(nullable = false, updatable = false, unique = true)
    private String cognitoSubject;

    @Column(nullable = false, updatable = false)
    private String cognitoUsername;

    @Column(nullable = false, unique = true)
    private String prettyId;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "TINYINT default false")
    private boolean isDeleted;

    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "TIMESTAMP default now()")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
