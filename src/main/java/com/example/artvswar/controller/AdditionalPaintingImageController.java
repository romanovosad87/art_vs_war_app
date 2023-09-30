package com.example.artvswar.controller;

import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.service.AdditionalImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/additionalPaintingImage")
@RequiredArgsConstructor
public class AdditionalPaintingImageController {
    private final AdditionalImageService additionalImageService;
    @PostMapping("/{paintingId}")
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AdditionalImageResponseDto> create(@PathVariable Long paintingId,
                                             @Valid @RequestBody List<ImageCreateRequestDto> dto) {
        return additionalImageService.saveAll(dto, paintingId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String delete(@PathVariable Long id) {
        return additionalImageService.delete(id);
    }

    @GetMapping("/folder/{paintingId}")
    @PreAuthorize("hasRole('AUTHOR')")
    public FolderResponseDto getFolder(@PathVariable Long paintingId) {
        return additionalImageService.createFolder(paintingId);
    }
}
