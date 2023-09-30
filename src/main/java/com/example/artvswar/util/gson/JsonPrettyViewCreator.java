package com.example.artvswar.util.gson;

import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class JsonPrettyViewCreator {
    public String create(Map result) {
       return new GsonBuilder().setPrettyPrinting().create().toJson(result);
    }
}
