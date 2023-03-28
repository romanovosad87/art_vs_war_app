package com.example.artvswar.controller;

import com.example.artvswar.model.Painting;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.service.PaintingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;
    private final PaintingService paintingService;

    public ImageController(ImageService imageService, PaintingService paintingService) {
        this.imageService = imageService;
        this.paintingService = paintingService;
    }

    @GetMapping("/save")
    public ResponseEntity<Object> saveImage(@RequestParam("extension") String extension) {
        return new ResponseEntity<>(imageService.generatePutUrl(extension), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getImage(@PathVariable Long id) {
        Painting painting = paintingService.get(id);
        return new ResponseEntity<>(imageService.generateGetUrl(painting.getImageFileName()),
                HttpStatus.OK);
    }
}
