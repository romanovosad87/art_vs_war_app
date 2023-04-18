package com.example.artvswar.service.impl;

import com.example.artvswar.model.Author;
import com.example.artvswar.repository.AuthorRepository;
import com.example.artvswar.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Author update(String id) {
//        Author author = authorRepository.findById(id).orElseThrow(
//                () -> new EntityNotFoundException(String.format("Can't find author by %s", id)));
//        return authorRepository.save(author);
        Author authorReference = authorRepository.getReferenceById(id);
        return authorRepository.save(authorReference);
    }

    @Override
    public Author get(String id) {
        return authorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find author by %s", id)));
    }

    @Override
    public Author getReferenceById(String id) {
        return authorRepository.getReferenceById(id);
    }

    @Override
    public Page<Author> getAll(PageRequest pageRequest) {
        return authorRepository.findAll(pageRequest);
    }


    @Override
    public long getNumberOfAllAuthors() {
        return authorRepository.count();
    }
}
