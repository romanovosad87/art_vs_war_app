package com.example.artvswar.model;

import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Table(name = "authors")
public class Author extends User {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String city;
    @Column(columnDefinition = "longtext")
    private String aboutMe;
    @Column(nullable = false, unique = true)
    @NaturalId
    private String imageFileName;
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "author")
    @JsonBackReference
    @ToString.Exclude
    private List<Painting> paintings = new ArrayList<>();
    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "TIMESTAMP default now()")
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addPainting(Painting painting) {
        paintings.add(painting);
        painting.setAuthor(this);
    }

    public void remove(Painting painting) {
        paintings.remove(painting);
        painting.setAuthor(null);
    }
}
