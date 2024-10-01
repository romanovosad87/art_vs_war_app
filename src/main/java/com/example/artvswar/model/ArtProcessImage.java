package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "art_process_image")
public class ArtProcessImage {
    @Id
    private Long id;

    @Column(nullable = false)
    private Double width;
    @Column(nullable = false)
    private Double height;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private ArtProcess artProcess;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
}
