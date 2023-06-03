package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Style;
import com.example.artvswar.repository.StyleRepository;
import com.example.artvswar.service.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StyleServiceImpl implements StyleService {
    private final StyleRepository repository;

    @Override
    @Transactional
    public Style save(Style style) {
        return repository.save(style);
    }

    @Override
    public Style get(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find style by id = %s", id)));
    }

    @Override
    public Style getReferenceById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public List<Style> getAll() {
        return repository.findAll();
    }
}
