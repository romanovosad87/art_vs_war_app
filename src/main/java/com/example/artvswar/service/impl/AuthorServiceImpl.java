package com.example.artvswar.service.impl;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.Picture;
import com.example.artvswar.repository.AuthorRepository;
import com.example.artvswar.service.AuthorService;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author get(Long id) {
        return authorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find author by %s", id)));
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}
