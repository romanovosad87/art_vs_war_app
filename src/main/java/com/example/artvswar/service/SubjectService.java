package com.example.artvswar.service;

import com.example.artvswar.model.Subject;
import java.util.List;

public interface SubjectService {
    Subject save(Subject subject);

    Subject get(Long id);

    Subject getReferenceById(Long id);

    List<Subject> getAll();
}
