package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.AuthorPhoto;
import com.example.artvswar.repository.AuthorPhotoRepository;
import com.example.artvswar.service.AuthorPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorPhotoServiceImpl implements AuthorPhotoService {
    private final AuthorPhotoRepository authorPhotoRepository;
    @Override
    public AuthorPhoto findById(Long id) {
        return authorPhotoRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find author photo by public_id: %s", id)));
    }
}
