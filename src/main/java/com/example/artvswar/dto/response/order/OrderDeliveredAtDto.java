package com.example.artvswar.dto.response.order;

import com.example.artvswar.util.gson.JsonLocalDateTimeOrderAdapter;
import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class OrderDeliveredAtDto {
    @JsonAdapter(JsonLocalDateTimeOrderAdapter.class)
    private OffsetDateTime orderDeliveredAt;
}
