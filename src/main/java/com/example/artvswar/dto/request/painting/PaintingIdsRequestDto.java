package com.example.artvswar.dto.request.painting;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
public class PaintingIdsRequestDto {
    Set<Long> ids = new HashSet<>();
}
