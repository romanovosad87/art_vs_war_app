package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.PaintingMapper;
import com.example.artvswar.dto.request.PaintingRequestDto;
import com.example.artvswar.dto.response.PaintingResponseDto;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.PaintingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paintings")
public class PaintingController {

    private final PaintingService paintingService;
    private final PaintingMapper paintingMapper;

    public PaintingController(PaintingService paintingService, PaintingMapper paintingMapper) {
        this.paintingService = paintingService;
        this.paintingMapper = paintingMapper;
    }

    @GetMapping("/all")
    public List<PaintingResponseDto> getAllPictures(){
        return paintingService.getAll().stream()
                .map(paintingMapper::toPictureResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PaintingResponseDto getById(@PathVariable Long id) {
        Painting painting = paintingService.get(id);
        return paintingMapper.toPictureResponseDto(painting);
    }

    @PostMapping
    public PaintingResponseDto create(@RequestBody PaintingRequestDto dto) {
        Painting painting = paintingMapper.toPictureModel(dto);
        return paintingMapper.toPictureResponseDto(paintingService.save(painting));
    }
}
