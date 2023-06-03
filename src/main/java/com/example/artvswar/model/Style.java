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
@Table(name = "styles")
public class Style {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(mappedBy = "styles")
    @JsonBackReference
    @ToString.Exclude
    private List<Painting> paintings = new ArrayList<>();

    public Style(String name) {
        this.name = name;
    }

    public void addPainting(Painting painting) {
        painting.getStyles().add(this);
        paintings.add(painting);
    }

    public void removePainting(Painting painting) {
        painting.getStyles().remove(this);
        paintings.remove(painting);
    }
}
