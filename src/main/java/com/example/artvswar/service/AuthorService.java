package com.example.artvswar.service;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.Picture;
import java.util.List;

public interface AuthorService {
    Author save(Author author);

    Author get(Long id);

    List<Author> getAllAuthors();
}
