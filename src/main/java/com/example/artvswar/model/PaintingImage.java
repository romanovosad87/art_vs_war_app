package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "painting_images")
public class PaintingImage {
    @Id
    private Long id;
    @Column(nullable = false)
    private Double width;
    @Column(nullable = false)
    private Double height;
    @Column(nullable = false)
    private Double initialRatio;
    @Column(nullable = false)
    private Double transformedRatio;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Painting painting;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
    @ElementCollection
    @CollectionTable(name = "room_views", joinColumns = @JoinColumn(name = "painting_image_id"),
    indexes = @Index(name = "image_roomView_image_id_idx", columnList = "painting_image_id"))
    @Cascade(CascadeType.ALL)
    private List<RoomView> roomViews = new ArrayList<>();
}
