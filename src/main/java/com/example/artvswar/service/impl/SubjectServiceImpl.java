package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Subject;
import com.example.artvswar.repository.SubjectRepository;
import com.example.artvswar.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    @Override
    @Transactional
    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject get(Long id) {
        return subjectRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find subject by id = %s", id)));
    }

    @Override
    public Subject getReferenceById(Long id) {
        return subjectRepository.getReferenceById(id);
    }

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }
}
