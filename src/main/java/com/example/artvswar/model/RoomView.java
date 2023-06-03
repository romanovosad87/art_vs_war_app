package com.example.artvswar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "imageUrl")
@Embeddable
public class RoomView {
    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Double ratio;
}
