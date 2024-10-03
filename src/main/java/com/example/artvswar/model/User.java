package com.example.artvswar.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "TINYINT(1) default false")
    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
