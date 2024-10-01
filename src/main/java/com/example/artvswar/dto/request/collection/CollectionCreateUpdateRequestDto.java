package com.example.artvswar.dto.request.collection;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import lombok.Data;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@GroupSequence({CollectionCreateUpdateRequestDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class CollectionCreateUpdateRequestDto {
    @NotBlank(message = "title field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 40,
            message = "title must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s\\w'!/-]+", message = "title except only Latin, digits, "
            + "space, ', !, / and -", groups = ThirdOrder.class)
    private String title;

    @NotNull(message = "description field is required", groups = FirstOrder.class)
    @Size(max = 1000,
            message = "description must have maximum 1000 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\w\\s\\p{P}\\p{S}]*", message = "description except only Latin",
            groups = ThirdOrder.class)
    private String description;
}
