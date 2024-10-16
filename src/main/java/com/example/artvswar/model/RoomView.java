package com.example.artvswar.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class RoomView {
    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String imageUrl;

}
