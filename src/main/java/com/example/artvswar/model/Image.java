package com.example.artvswar.model;

import com.example.artvswar.model.enummodel.ModerationStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Entity
@ToString
@Table(name = "images",
        indexes = {@Index(name = "IDX_moderation_status", columnList = "moderationStatus")})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String publicId;
    @Column(columnDefinition = "VARCHAR(500)", nullable = false, unique = true)
    private String url;

    @Column(columnDefinition = "TINYINT NOT NULL DEFAULT 0")
    private ModerationStatus moderationStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
