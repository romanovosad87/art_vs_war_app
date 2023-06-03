package com.example.artvswar.dto.request;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import lombok.Data;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

@Data
@GroupSequence({ImageRequestDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class ImageRequestDto {
    @NotNull(message = "public_id field is required", groups = FirstOrder.class)
    private String publicId;
    @NotNull(message = "width field is required", groups = FirstOrder.class)
    private Double width;
    @NotNull(message = "height field is required", groups = FirstOrder.class)
    private Double height;
    @NotNull(message = "version field is required", groups = FirstOrder.class)
    private String version;
    @NotNull(message = "signature field is required", groups = FirstOrder.class)
    private String signature;
}
