package com.example.artvswar.dto.request.collection;

import lombok.Data;
import java.util.List;
import javax.validation.constraints.NotNull;

@Data
public class CollectionAddRemovePaintingsRequestDto {
    @NotNull(message = "painting id field is required")
    private List<Long> paintingsIds;
}
