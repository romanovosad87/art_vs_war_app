package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Medium;
import com.example.artvswar.repository.MediumRepository;
import com.example.artvswar.service.MediumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediumServiceImpl implements MediumService {
    private final MediumRepository repository;

    @Override
    @Transactional
    public Medium save(Medium medium) {
        return repository.save(medium);
    }

    @Override
    public Medium get(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find medium by id = %s", id)));
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
