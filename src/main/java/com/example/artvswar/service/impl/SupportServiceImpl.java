package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Support;
import com.example.artvswar.repository.SupportRepository;
import com.example.artvswar.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupportServiceImpl implements SupportService {
    private final SupportRepository repository;

    @Override
    @Transactional
    public Support save(Support support) {
        return repository.save(support);
    }

    @Override
    public Support get(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find support by id = %s", id)));
    }

    @Override
    public Support getReferenceById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public List<Support> getAll() {
        return repository.findAll();
    }
}
