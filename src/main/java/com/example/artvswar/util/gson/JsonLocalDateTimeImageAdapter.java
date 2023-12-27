package com.example.artvswar.util.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class JsonLocalDateTimeImageAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.beginObject();
        out.name("createdAt");
        out.value(formatter.format(value));
        out.endObject();
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        return null;
    }
}
