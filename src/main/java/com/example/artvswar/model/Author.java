package com.example.artvswar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE authors SET is_deleted = true where cognito_subject = ?",
        check = ResultCheckStyle.COUNT)
@Where(clause = "is_deleted = false")
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

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY)
    @Cascade({CascadeType.ALL})
    private AuthorPhoto authorPhoto;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonManagedReference
    private List<ArtProcess> artProcesses = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "author")
    @JsonBackReference
    @ToString.Exclude
    private List<Painting> paintings = new ArrayList<>();

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JsonBackReference
    private StripeProfile stripeProfile;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TINYINT not null default false")
    private boolean unsubscribedEmail;

    public void addPainting(Painting painting) {
        paintings.add(painting);
        painting.setAuthor(this);
    }

    public void removePainting(Painting painting) {
        paintings.remove(painting);
        painting.setAuthor(null);
    }

    public void addArtProcess(ArtProcess artProcess) {
        artProcesses.add(artProcess);
        artProcess.setAuthor(this);
    }

    public void removeArtProcess(ArtProcess artProcess) {
        artProcesses.remove(artProcess);
        artProcess.setAuthor(null);
    }
}
