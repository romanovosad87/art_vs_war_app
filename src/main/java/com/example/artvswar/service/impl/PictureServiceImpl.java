package com.example.artvswar.service.impl;

import com.example.artvswar.model.Picture;
import com.example.artvswar.repository.PictureRepository;
import com.example.artvswar.service.PictureService;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;

    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Picture get(Long id) {
        return pictureRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find picture by %s", id)));
    }

    @Override
    public List<Picture> getAll() {
        return pictureRepository.findAll();
    }
}
