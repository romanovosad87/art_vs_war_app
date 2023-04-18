package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "authors")
public class Author {
    @Id
    private String id;
    private String fullName;
    private String country;
    private String city;
    private String aboutMe;
    @Column(unique = true)
    private String imageFileName;
    @OneToMany(mappedBy = "author")
    private List<Painting> paintings = new ArrayList<>();
    @UpdateTimestamp
    private LocalDateTime createdAt;

    public void addPainting(Painting painting) {
        paintings.add(painting);
        painting.setAuthor(this);
    }
//
//    public void remove(Painting painting) {
//        paintings.remove(painting);
//        painting.setAuthor(null);
//    }
}
