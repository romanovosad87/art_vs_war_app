package com.example.artvswar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    @JsonManagedReference
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
