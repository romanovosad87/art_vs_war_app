package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "mock_rooms")
public class MockRoom {
    @Id
    private Long id;

    @Column(unique = true)
    private String publicId;
}
