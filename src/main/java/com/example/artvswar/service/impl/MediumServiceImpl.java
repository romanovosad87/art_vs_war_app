package com.example.artvswar.service.impl;

import com.example.artvswar.model.Medium;
import com.example.artvswar.repository.MediumRepository;
import com.example.artvswar.service.MediumService;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class MediumServiceImpl implements MediumService {
    private final MediumRepository repository;

    public MediumServiceImpl(MediumRepository repository) {
        this.repository = repository;
    }

    @Override
    public Medium save(Medium medium) {
        return repository.save(medium);
    }

    @Override
    public Medium get(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find medium by %s", id)));
    }

    @Override
    public Medium getReferenceById(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public List<Medium> getAll() {
        return repository.findAll();
    }
}
