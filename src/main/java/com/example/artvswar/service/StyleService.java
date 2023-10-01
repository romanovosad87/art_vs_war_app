package com.example.artvswar.service;

import com.example.artvswar.model.Style;
import java.util.List;

public interface StyleService {
    Style save(Style style);

    Style get(Long id);

    Style getReferenceById(Long id);

    List<Style> getAll();
}
