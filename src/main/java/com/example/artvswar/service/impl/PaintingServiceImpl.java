package com.example.artvswar.service.impl;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.PictureRepository;
import com.example.artvswar.service.PaintingService;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class PaintingServiceImpl implements PaintingService {

    private final PictureRepository pictureRepository;

    public PaintingServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Painting save(Painting painting) {
        return pictureRepository.save(painting);
    }

    @Override
    public Painting get(Long id) {
        return pictureRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find picture by %s", id)));
    }

    @Override
    public List<Painting> getAll() {
        return pictureRepository.findAll();
    }
}
