package com.example.artvswar.dto.request.painting;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import com.example.artvswar.lib.Year;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.GroupSequence;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@GroupSequence({PaintingCheckInputDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class PaintingCheckInputDto {
    @NotBlank(message = "title field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 40,
            message = "title must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\w\\s\\p{P}\\p{S}]+", message = "title except only Latin "
            , groups = ThirdOrder.class)
    private String title;

    @NotNull(message = "price field is required", groups = FirstOrder.class)
    @Digits(integer = 5, fraction = 0, message = "price must have maximum 5 number of digits "
            + "without cents", groups = ThirdOrder.class)
    private BigDecimal price;

    @Size(max = 1000,
            message = "description must have maximum 1000 characters", groups = ThirdOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\w\\s\\p{P}\\p{S}]*", message = "description except only Latin",
            groups = ThirdOrder.class)
    private String description;

    @NotNull(message = "style field is required", groups = FirstOrder.class)
    private List<Long> styleIds;
    @NotNull(message = "medium field is required", groups = FirstOrder.class)
    private List<Long> mediumIds;
    @NotNull(message = "support field is required", groups = FirstOrder.class)
    private List<Long> supportIds;

    @NotNull(message = "subject field is required", groups = FirstOrder.class)
    private List<Long> subjectIds;
    @NotNull(message = "weight field is required", groups = FirstOrder.class)
    @Min(value = 1, message = "min weight is 1 gram", groups = ThirdOrder.class)
    @Max(value = 10_000, message = "max weight is 10_000 gram", groups = ThirdOrder.class)
    @Digits(integer = 5, fraction = 0, message = "weight must have maximum 5 number of digits "
            + "of integer numbers", groups = ThirdOrder.class)
    private Double weight;

    @NotNull(message = "width field is required", groups = FirstOrder.class)
    @Min(value = 1, message = "min width is 1 cm", groups = ThirdOrder.class)
    @Max(value = 200, message = "max width is 200 cm", groups = ThirdOrder.class)
    @Digits(integer = 3, fraction = 0, message = "width must have maximum 3 number of digits "
            + "of integer numbers", groups = ThirdOrder.class)
    private Double width;

    @NotNull(message = "height field is required", groups = FirstOrder.class)
    @Min(value = 1, message = "min height is 1 cm", groups = ThirdOrder.class)
    @Max(value = 200, message = "max height is 200 cm", groups = ThirdOrder.class)
    @Digits(integer = 3, fraction = 0, message = "height must have maximum 3 number of digits "
            + "of integer numbers", groups = ThirdOrder.class)
    private Double height;

    @NotNull(message = "depth field is required", groups = FirstOrder.class)
    @Min(value = 1, message = "min depth is 1 cm", groups = ThirdOrder.class)
    @Max(value = 10, message = "max depth is 10 cm", groups = ThirdOrder.class)
    @Digits(integer = 2, fraction = 0, message = "depth must have maximum 2 number of digits "
            + "of integer numbers", groups = ThirdOrder.class)
    private Double depth;

    @NotNull(message = "year of creation field is required", groups = FirstOrder.class)
    @Year(groups = ThirdOrder.class)
    @Min(value = 1000, message = "min value for year of creation must be 1000", groups = ThirdOrder.class)
    private Integer yearOfCreation;
}
