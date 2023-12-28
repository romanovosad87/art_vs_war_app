package com.example.artvswar.util.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class JsonLocalDateTimeOrderDeliveredAtAdapter extends TypeAdapter<OffsetDateTime> {
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Override
    public void write(JsonWriter out, OffsetDateTime value) throws IOException {
        out.beginObject();
        out.name("orderDeliveredAt");
        out.value(formatter.format(value));
        out.endObject();
    }

    @Override
    public OffsetDateTime read(JsonReader in) throws IOException {
        return null;
    }
}
