package com.example.artvswar.service.impl;

import com.example.artvswar.model.Support;
import com.example.artvswar.repository.SupportRepository;
import com.example.artvswar.service.SupportService;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class SupportServiceImpl implements SupportService {
    private final SupportRepository repository;

    public SupportServiceImpl(SupportRepository repository) {
        this.repository = repository;
    }

    @Override
    public Support save(Support support) {
        return repository.save(support);
    }

    @Override
    public Support get(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find support by %s", id)));
    }

    @Override
    public List<Support> getAll() {
        return repository.findAll();
    }
}
