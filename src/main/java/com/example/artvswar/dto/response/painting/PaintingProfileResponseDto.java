package com.example.artvswar.dto.response.painting;

import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.collection.CollectionInPaintingResponseDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.model.enumModel.PaymentStatus;
import com.example.artvswar.util.gson.JsonLocalDateTimePaintingAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaintingProfileResponseDto {
    private Long id;
    private String prettyId;
    private String title;
    private String description;
    private BigDecimal price;
    private Double weight;
    private Double height;
    private Double width;
    private Double depth;
    private PaymentStatus paymentStatus;
    private Integer yearOfCreation;
    @JsonAdapter(JsonLocalDateTimePaintingAdapter.class)
    private LocalDateTime addedToDatabase;
    private AuthorForPaintingResponseDto author;
    private CollectionInPaintingResponseDto collection;
    private List<IdValuePair> styles;
    private List<IdValuePair> mediums;
    private List<IdValuePair> supports;
    private List<IdValuePair> subjects;
    @SerializedName(value = "image")
    private PaintingImageResponseDto paintingImageResponseDto;

    @AllArgsConstructor
    public static class IdValuePair {
        private Long id;
        private String value;
    }
}
