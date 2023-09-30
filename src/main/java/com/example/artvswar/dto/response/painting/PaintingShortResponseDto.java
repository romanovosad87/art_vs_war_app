package com.example.artvswar.dto.response.painting;

import com.example.artvswar.model.enumModel.PaymentStatus;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PaintingShortResponseDto {
    private Long id;
    private String prettyId;
    private String title;
    private BigDecimal price;
    private Double width;
    private Double height;
    private Double depth;
    private Integer yearOfCreation;
    private PaymentStatus paymentStatus;
    @SerializedName(value = "imageId")
    private String paintingImageImagePublicId;
    @SerializedName(value = "imageUrl")
    private String paintingImageImageUrl;
    private String authorFullName;
    private String authorPrettyId;
    private String authorCountry;
}
