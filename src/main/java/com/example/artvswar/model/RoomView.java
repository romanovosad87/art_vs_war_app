package com.example.artvswar.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class RoomView {
    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    private String imageUrl;

}
