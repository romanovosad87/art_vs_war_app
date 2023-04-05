package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;
    private String city;
    private String shortStory;
    @Column(unique = true)
    private String email;
    @OneToMany(mappedBy = "author")
    private List<Painting> paintings = new ArrayList<>();

//    public void addPainting(Painting painting) {
//        paintings.add(painting);
//        painting.setAuthor(this);
//    }
//
//    public void remove(Painting painting) {
//        paintings.remove(painting);
//        painting.setAuthor(null);
//    }
}
