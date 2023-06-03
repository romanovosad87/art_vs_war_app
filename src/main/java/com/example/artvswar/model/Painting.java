package com.example.artvswar.model;

import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "image")
@Entity
@Table(name = "paintings", indexes = {
        @Index(name = "price_idx", columnList = "price"),
        @Index(name = "width_idx", columnList = "width"),
        @Index(name = "height_idx", columnList = "height")
})
public class Painting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(columnDefinition = "longtext")
    private String description;
    @Column(nullable = false)
    private Double width;
    @Column(nullable = false)
    private Double height;
    @Column(nullable = false)
    private Integer yearOfCreation;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "painting_author_fk"))
    @JsonManagedReference
    private Author author;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany
    @JoinTable(
            name = "paintings_styles",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    @JsonManagedReference
    private Set<Style> styles = new HashSet<>();

    @ManyToMany
    @Setter(AccessLevel.PRIVATE)
    @JoinTable(
            name = "paintings_mediums",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "medium_id")
    )
    @JsonManagedReference
    private Set<Medium> mediums = new HashSet<>();

    @ManyToMany
    @Setter(AccessLevel.PRIVATE)
    @JoinTable(
            name = "paintings_supports",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "support_id")
    )
    @JsonManagedReference
    private Set<Support> supports = new HashSet<>();

    @ManyToMany
    @Setter(AccessLevel.PRIVATE)
    @JoinTable(
            name = "paintings_subjects",
            joinColumns = @JoinColumn(name = "painting_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @JsonManagedReference
    private Set<Subject> subjects = new HashSet<>();

    @OneToOne(optional = false, fetch = FetchType.LAZY, orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    @JoinColumn(name = "image_id")
    private Image image;

    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "TIMESTAMP default now()")
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    private LocalDateTime entityCreatedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TINYINT NOT NULL default false")
    private boolean isSold;

    public void addStyle(Style style) {
        style.getPaintings().add(this);
        styles.add(style);
    }

    public void removeStyle(Style style) {
        style.getPaintings().remove(this);
        styles.remove(style);
    }

    public void addMedium(Medium medium) {
        medium.getPaintings().add(this);
        mediums.add(medium);
    }

    public void removeMedium(Medium medium) {
        medium.getPaintings().remove(this);
        mediums.remove(medium);
    }

    public void addSupport(Support support) {
        support.getPaintings().add(this);
        supports.add(support);
    }

    public void removeSupport(Support support) {
        support.getPaintings().remove(this);
        supports.remove(support);
    }

    public void addSubject(Subject subject) {
        subject.getPaintings().add(this);
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subject.getPaintings().remove(this);
        subjects.remove(subject);
    }
}
