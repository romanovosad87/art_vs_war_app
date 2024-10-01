package com.example.artvswar.model;

import com.example.artvswar.model.enummodel.PaymentStatus;
import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "prettyId")
@Entity
@Table(name = "paintings", indexes = {
            @Index(name = "IDX_price", columnList = "price"),
        @Index(name = "IDX_width", columnList = "width"),
        @Index(name = "IDX_height", columnList = "height"),
        @Index(name = "IDX_payment_status", columnList = "paymentStatus")
})
public class Painting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String prettyId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false, columnDefinition = "longtext")
    private String description;
    @Column(nullable = false)
    private Double weight;
    @Column(nullable = false)
    private Double width;
    @Column(nullable = false)
    private Double height;
    @Column(nullable = false)
    private Double depth;
    @Column(nullable = false)
    private Integer yearOfCreation;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany
    @JoinTable(
            name = "paintings_styles",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    private Set<Style> styles = new HashSet<>();

    @ManyToMany
    @Setter(AccessLevel.PRIVATE)
    @JoinTable(
            name = "paintings_mediums",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "medium_id")
    )
    private Set<Medium> mediums = new HashSet<>();

    @ManyToMany
    @Setter(AccessLevel.PRIVATE)
    @JoinTable(
            name = "paintings_supports",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "support_id")
    )
    private Set<Support> supports = new HashSet<>();

    @ManyToMany
    @Setter(AccessLevel.PRIVATE)
    @JoinTable(
            name = "paintings_subjects",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    @OneToOne(mappedBy = "painting")
    @Cascade({CascadeType.ALL})
    @JoinColumn(name = "image_id")
    private PaintingImage paintingImage;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "painting", orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    private List<AdditionalImage> additionalImages = new ArrayList<>();

    @CreationTimestamp
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    private LocalDateTime entityCreatedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TINYINT NOT NULL DEFAULT 0")
    private PaymentStatus paymentStatus;

    @OneToMany(mappedBy = "painting")
    Set<ShoppingCartPainting> shoppingCarts = new HashSet<>();

    public Optional<Collection> getCollection() {
        return Optional.ofNullable(collection);
    }

    public void addStyle(Style style) {
        styles.add(style);
    }

    public void addMedium(Medium medium) {
        mediums.add(medium);
    }

    public void addSupport(Support support) {
        supports.add(support);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void addAdditionalImage(AdditionalImage image) {
        additionalImages.add(image);
        image.setPainting(this);
    }

    public void remove(AdditionalImage image) {
        additionalImages.remove(image);
        image.setPainting(null);
    }

    public void addPaintingImage(PaintingImage paintingImage) {
        this.paintingImage = paintingImage;
        paintingImage.setPainting(this);
    }

    public void remove(PaintingImage paintingImage) {
        this.paintingImage = null;
        paintingImage.setPainting(null);
    }
}
