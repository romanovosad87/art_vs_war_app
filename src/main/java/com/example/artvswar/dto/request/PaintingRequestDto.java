package com.example.artvswar.dto.request;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import com.example.artvswar.lib.Year;
import lombok.Data;
import java.math.BigDecimal;
import javax.validation.GroupSequence;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@GroupSequence({PaintingRequestDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class PaintingRequestDto {
    @NotBlank(message = "title field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 40,
            message = "title must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s\\w'!/-]+", message = "title except only Latin, digits, "
            + "space, ', !, / and -", groups = ThirdOrder.class)
    private String title;

    @NotNull(message = "price field is required", groups = FirstOrder.class)
    @Digits(integer = 8, fraction = 0, message = "price must have maximum 8 number of digits "
            + "without cents", groups = ThirdOrder.class)
    private BigDecimal price;

    @Size(max = 1000,
            message = "description must have maximum 1000 characters", groups = ThirdOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\w\\s\\p{P}\\p{S}]*", message = "description except only Latin",
    groups = ThirdOrder.class)
    private String description;

    @NotNull(message = "style field is required", groups = FirstOrder.class)
    private Long styleId;
    @NotNull(message = "medium field is required", groups = FirstOrder.class)
    private Long mediumId;
    @NotNull(message = "support field is required", groups = FirstOrder.class)
    private Long supportId;

    @NotNull(message = "height field is required", groups = FirstOrder.class)
    @Min(value = 1, message = "min height is 1 cm", groups = ThirdOrder.class)
    @Max(value = 700, message = "max height is 700 cm", groups = ThirdOrder.class)
    private Integer height;

    @NotNull(message = "width field is required", groups = FirstOrder.class)
    @Min(value = 1, message = "min width is 1 cm", groups = ThirdOrder.class)
    @Max(value = 700, message = "max width is 700 cm", groups = ThirdOrder.class)
    private Integer width;

    @NotNull(message = "year of creation field is required", groups = FirstOrder.class)
    @Year(groups = ThirdOrder.class)
    @Min(value = 1000, message = "min value for year of creation must be 1000", groups = ThirdOrder.class)
    private Integer yearOfCreation;

    @NotNull(message = "image field is required", groups = FirstOrder.class)
    private String imageFileName;
}
