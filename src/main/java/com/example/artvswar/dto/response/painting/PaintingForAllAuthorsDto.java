package com.example.artvswar.dto.response.painting;

import com.example.artvswar.dto.response.StyleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PaintingForAllAuthorsDto {
    private Long id;
    private List<StyleResponseDto> styles = new ArrayList<>();

    public PaintingForAllAuthorsDto(Long id) {
        this.id = id;
    }
}
