package com.example.artvswar.service;

import com.example.artvswar.model.Support;
import java.util.List;

public interface SupportService {
    Support save(Support support);

    Support get(Long id);

    Support getReferenceById(Long id);

    List<Support> getAll();
}
