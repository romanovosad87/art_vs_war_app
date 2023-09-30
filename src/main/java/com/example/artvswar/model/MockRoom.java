package com.example.artvswar.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
