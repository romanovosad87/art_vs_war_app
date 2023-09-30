package com.example.artvswar.util.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class JsonLocalDateTimePaintingAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void write(JsonWriter out, LocalDateTime localDateTime)
            throws IOException {
        out.beginObject();
        out.name("addedToDatabase");
        out.value(formatter.format(localDateTime));
        out.endObject();
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
