package com.example.artvswar.service;

import com.example.artvswar.model.Picture;
import java.util.List;

public interface PictureService {
    Picture save(Picture picture);

    Picture get(Long id);

    List<Picture> getAll();
}
