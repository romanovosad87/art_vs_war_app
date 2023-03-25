package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.PictureMapper;
import com.example.artvswar.dto.request.PictureRequestDto;
import com.example.artvswar.dto.response.PictureResponseDto;
import com.example.artvswar.model.Picture;
import com.example.artvswar.service.PictureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pictures")
public class PictureController {

    private final PictureService pictureService;
    private final PictureMapper pictureMapper;

    public PictureController(PictureService pictureService, PictureMapper pictureMapper) {
        this.pictureService = pictureService;
        this.pictureMapper = pictureMapper;
    }

    @GetMapping("/all")
    public List<PictureResponseDto> getAllPictures(){
        return pictureService.getAll().stream()
                .map(pictureMapper::toPictureResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PictureResponseDto getById(@PathVariable Long id) {
        Picture picture = pictureService.get(id);
        return pictureMapper.toPictureResponseDto(picture);
    }

    @PostMapping
    public PictureResponseDto create(@RequestBody PictureRequestDto dto) {
        Picture picture = pictureMapper.toPictureModel(dto);
        return pictureMapper.toPictureResponseDto(pictureService.save(picture));
    }
}
