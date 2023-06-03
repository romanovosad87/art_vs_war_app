package com.example.artvswar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "images")
public class Image {
    @Id
    @NaturalId
    private String id;
    @Column(nullable = false)
    private Double width;
    @Column(nullable = false)
    private Double height;
    @Column(nullable = false)
    private Double initialRatio;
    @Column(nullable = false)
    private Double transformedRatio;
    @ElementCollection
    @CollectionTable(name = "room_views", joinColumns = @JoinColumn(name = "image_id"),
    indexes = @Index(name = "image_roomView_image_id_idx", columnList = "image_id"))
    private Set<RoomView> roomViews = new HashSet<>();
}
