package com.example.artvswar.dto.response.painting;

import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.collection.CollectionInPaintingResponseDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.model.enumModel.PaymentStatus;
import com.example.artvswar.util.gson.JsonLocalDateTimePaintingAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaintingResponseDto {
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
    private List<String> styles;
    private List<String> mediums;
    private List<String> supports;
    private List<String> subjects;
    @SerializedName(value = "image")
    private PaintingImageResponseDto paintingImageResponseDto;
}
