package com.example.artvswar.model;

import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "paintings")
public class Painting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private int height;
    private int width;
    private int yearOfCreation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_id")
    private Style style;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium_id")
    private Medium medium;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id")
    private Support support;
//    @Column(unique = true)
    private String imageFileName;
    @UpdateTimestamp
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    private LocalDateTime entityCreatedAt;

    public Painting(String title, BigDecimal price, String description, int height, int width,
                    int yearOfCreation, Author author, Style style, Medium medium, Support support,
                    String imageFileName) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.height = height;
        this.width = width;
        this.yearOfCreation = yearOfCreation;
        this.author = author;
        this.style = style;
        this.medium = medium;
        this.support = support;
        this.imageFileName = imageFileName;
    }
}
