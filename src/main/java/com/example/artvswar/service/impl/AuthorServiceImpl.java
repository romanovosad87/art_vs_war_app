package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Author;
import com.example.artvswar.repository.AuthorRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.util.UrlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final UrlParser urlParser;
    private final ImageService imageService;

    @Override
    @Transactional
    public Author save(Author author) {
        return authorRepository.save(author);
    }


    @Override
    @Transactional
    public Author update(Author author) {
        Author authorFromDb = authorRepository.findById(author.getId()).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find author by id = %s",
                        author.getId())));
        String imageFileNameFromDb = authorFromDb.getImageFileName();
        if (author.getImageFileName() == null) {
            author.setImageFileName(imageFileNameFromDb);
        } else {
            if (imageFileNameFromDb != null) {
                imageService.delete(imageFileNameFromDb);
            }
        }
        return authorRepository.save(author);
    }

    @Override
    public Author get(String id) {
        return authorRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find author by id = %s", id)));
    }


    @Override
    public Page<Author> getAll(Map<String, String> params) {
        PageRequest pageRequest = urlParser.getPageRequest(params);
        return authorRepository.findAll(pageRequest);
    }


    @Override
    public long getNumberOfAllAuthors() {
        return authorRepository.count();
    }
}
