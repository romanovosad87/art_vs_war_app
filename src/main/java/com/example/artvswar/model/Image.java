package com.example.artvswar.model;

import com.example.artvswar.model.enumModel.ModerationStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Entity
@Table(name = "images",
        indexes = {@Index(name = "moderation_status", columnList = "moderationStatus")})
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
