package com.example.artvswar.service;

import com.example.artvswar.model.Author;
import org.springframework.data.domain.Page;
import java.util.Map;

public interface AuthorService {
    Author save(Author author);

    Author update(Author author);

    Author get(String id);

    Page<Author> getAll(Map<String, String> params);

    long getNumberOfAllAuthors();
}
