package com.example.artvswar.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String prettyId;

    @Column(nullable = false, columnDefinition = "longtext")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Author author;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "collection")
    private Set<Painting> paintings = new HashSet<>();

    public void addPainting(Painting painting) {
        paintings.add(painting);
        painting.setCollection(this);
    }

    public void remove(Painting painting) {
        paintings.remove(painting);
        painting.setCollection(null);
    }
}
