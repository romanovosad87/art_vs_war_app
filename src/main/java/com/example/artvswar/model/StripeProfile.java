package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "stripe_profiles", indexes = {
        @Index(name = "IDX_is_detail_submitted", columnList = "isDetailsSubmitted")
})
public class StripeProfile {
    @Id
    private Long id;
    @Column(unique = true, updatable = false, nullable = false)
    private String accountId;
    @Column(columnDefinition = "TINYINT(1) NOT NULL default false")
    private boolean isDetailsSubmitted;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Author author;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
