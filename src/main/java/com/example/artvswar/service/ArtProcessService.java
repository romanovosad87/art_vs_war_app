package com.example.artvswar.service;

import com.example.artvswar.dto.request.artProcess.ArtProcessCreateRequestDto;
import com.example.artvswar.dto.request.artProcess.ArtProcessUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.artProcess.ArtProcessResponseDto;
import java.util.List;

public interface ArtProcessService {
    ArtProcessResponseDto save(ArtProcessCreateRequestDto dto, String cognitoSubject);
    ArtProcessResponseDto update(Long id, ArtProcessUpdateRequestDto dto, String cognitoSubject);
    void delete(Long id);

    ArtProcessResponseDto getById(Long id);

    List<ArtProcessResponseDto> getAllByAuthorPrettyId(String authorPrettyId);

    List<ArtProcessResponseDto> getAllByAuthorCognitoSubject(String cognitoSubject);

    FolderResponseDto createCloudinaryFolder(String cognitoSubject);
}
