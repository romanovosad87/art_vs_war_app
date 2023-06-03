package com.example.artvswar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@Entity
@Table(name = "supports")
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(mappedBy = "supports")
    @JsonBackReference
    @ToString.Exclude
    private List<Painting> paintings = new ArrayList<>();

    public Support(String name) {
        this.name = name;
    }

    public void addPainting(Painting painting) {
        painting.getSupports().add(this);
        paintings.add(painting);
    }

    public void removePainting(Painting painting) {
        painting.getSupports().remove(this);
        paintings.remove(painting);
    }
}
