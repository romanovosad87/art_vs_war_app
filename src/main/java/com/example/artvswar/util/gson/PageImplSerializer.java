package com.example.artvswar.util.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageImpl;
import java.lang.reflect.Type;

@JsonComponent
public class PageImplSerializer implements JsonSerializer<PageImpl<?>> {
    @Override
    public JsonElement serialize(PageImpl page, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
//        jsonObject.add("content", (JsonElement) page.getContent());
        jsonObject.addProperty("first", page.isFirst());
        jsonObject.addProperty("last", page.isLast());
        jsonObject.addProperty("totalPages", page.getTotalPages());
        jsonObject.addProperty("totalElements", page.getTotalElements());
        jsonObject.addProperty("numberOfElements", page.getNumberOfElements());
        return jsonObject;
    }
}

// jsonGenerator.writeStartObject();
//         jsonGenerator.writeObjectField("content", page.getContent());
//         jsonGenerator.writeBooleanField("first", page.isFirst());
//         jsonGenerator.writeBooleanField("last", page.isLast());
//         jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
//         jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
//         jsonGenerator.writeNumberField("numberOfElements", page.getNumberOfElements());
//
//         jsonGenerator.writeNumberField("size", page.getSize());
//         jsonGenerator.writeNumberField("number", page.getNumber());
//
//         Sort sort = page.getSort();
//
//         jsonGenerator.writeArrayFieldStart("sort");
//
//         for (Sort.Order order : sort) {
//         jsonGenerator.writeStartObject();
//         jsonGenerator.writeStringField("property", order.getProperty());
//         jsonGenerator.writeStringField("direction", order.getDirection().name());
//         jsonGenerator.writeBooleanField("ignoreCase", order.isIgnoreCase());
//         jsonGenerator.writeStringField("nullHandling", order.getNullHandling().name());
//         jsonGenerator.writeEndObject();
//         }
//
//         jsonGenerator.writeEndArray();
//         jsonGenerator.writeEndObject();
//         }
