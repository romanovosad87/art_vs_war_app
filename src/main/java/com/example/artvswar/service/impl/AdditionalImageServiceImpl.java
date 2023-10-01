package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.AdditionalImageMapper;
import com.example.artvswar.dto.request.image.ImageCreateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.AdditionalImageRepository;
import com.example.artvswar.service.AdditionalImageService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.CloudinaryFolderCreator;
import com.example.artvswar.util.image.CloudinaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdditionalImageServiceImpl implements AdditionalImageService {

    private final AdditionalImageRepository additionalImageRepository;
    private final AdditionalImageMapper additionalImageMapper;
    private final CloudinaryClient cloudinaryClient;
    private final PaintingService paintingService;
    private final CloudinaryFolderCreator cloudinaryFolderCreator;

    @Override
    @Transactional
    public List<AdditionalImageResponseDto> saveAll(List<ImageCreateRequestDto> dtos, Long id) {

        List<AdditionalImage> additionalImages = dtos.stream()
                .map(image -> additionalImageMapper.toModel(image, id))
                .collect(Collectors.toList());

        List<AdditionalImage> savedAdditionalImages = additionalImageRepository.saveAll(additionalImages);


        return savedAdditionalImages.stream()
                .map(additionalImageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String delete(Long id) {
        AdditionalImage additionalImage = additionalImageRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find additional image by id = %s", id)));
        additionalImageRepository.deleteById(id);
        String publicId = additionalImage.getImage().getPublicId();
        String delete = cloudinaryClient.delete(publicId);
        if (delete.equals("ok")) {
            return String.format("Asset with public id: %s was deleted from Cloudinary", publicId);
        }
        return String.format("Asset with public id: %s was NOT deleted from Cloudinary", publicId);
    }

    @Override
    public FolderResponseDto createFolder(Long paintingId) {
        Painting painting = paintingService.get(paintingId);
        String publicId = painting.getPaintingImage().getImage().getPublicId();
        return cloudinaryFolderCreator.createForAdditionalImage(publicId);
    }
}
