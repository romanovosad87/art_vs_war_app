package com.example.artvswar.service;

import com.example.artvswar.model.Medium;
import java.util.List;

public interface MediumService {
    Medium save(Medium medium);

    Medium get(Long id);

    Medium getReferenceById(Long id);

    List<Medium> getAll();
}
