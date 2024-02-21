package com.example.artvswar.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
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
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE authors SET is_deleted = true where id = ?",
        check = ResultCheckStyle.COUNT)
//@Where(clause = "is_deleted = false")
@Table(name = "authors")
public class Author extends User {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String prettyId;
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
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private AuthorPhoto authorPhoto;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private List<ArtProcess> artProcesses = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private List<Painting> paintings = new ArrayList<>();

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private StripeProfile stripeProfile;

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    private AuthorShippingAddress authorShippingAddress;

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
