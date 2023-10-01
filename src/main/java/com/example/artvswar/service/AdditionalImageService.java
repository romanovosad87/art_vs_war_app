package com.example.artvswar.service;

import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import java.util.List;

public interface AdditionalImageService {
    List<AdditionalImageResponseDto>  saveAll(List<ImageCreateRequestDto> dto, Long id);

    String delete(Long id);

    FolderResponseDto createFolder(Long paintingId);
}
