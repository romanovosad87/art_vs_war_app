package com.example.artvswar.service;

import com.example.artvswar.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AuthorService {
    Author save(Author author);

    Author update(String id);

    Author get(String id);

    Author getReferenceById(String id);

    Page<Author> getAll(PageRequest pageRequest);

    long getNumberOfAllAuthors();
}
