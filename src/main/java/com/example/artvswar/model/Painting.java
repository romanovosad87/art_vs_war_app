package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "paintings")
public class Painting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;
    @ManyToOne(fetch = FetchType.LAZY)
    private Style style;
    @ManyToOne(fetch = FetchType.LAZY)
    private Medium medium;
    @ManyToOne(fetch = FetchType.LAZY)
    private Support support;
    private int height;
    private int width;
    @Column(unique = true)
    private String imageFileName;
}
