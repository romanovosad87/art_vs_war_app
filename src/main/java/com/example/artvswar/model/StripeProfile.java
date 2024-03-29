package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
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
@Table(name = "stripe_profiles", indexes = {
        @Index(name = "IDX_is_detail_submitted", columnList = "isDetailsSubmitted")
})
public class StripeProfile {
    @Id
    private Long id;
    @Column(unique = true, updatable = false, nullable = false)
    private String accountId;
    @Column(columnDefinition = "TINYINT NOT NULL default false")
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
