package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.ArtProcessMapper;
import com.example.artvswar.dto.request.artProcess.ArtProcessCreateRequestDto;
import com.example.artvswar.dto.request.artProcess.ArtProcessUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.artProcess.ArtProcessResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.ArtProcess;
import com.example.artvswar.repository.ArtProcessRepository;
import com.example.artvswar.service.ArtProcessService;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.util.CloudinaryFolderCreator;
import com.example.artvswar.util.image.CloudinaryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtProcessServiceImpl implements ArtProcessService {
    private final ArtProcessRepository artProcessRepository;
    private final AuthorService authorService;
    private final CloudinaryClient cloudinaryClient;
    private final ArtProcessMapper artProcessMapper;
    private final CloudinaryFolderCreator cloudinaryFolderCreator;

    @Override
    @Transactional
    public ArtProcessResponseDto save(ArtProcessCreateRequestDto dto, String cognitoSubject) {
        ArtProcess artProcess = artProcessMapper.toModel(dto);
        artProcess.setAuthor(authorService.getAuthorByCognitoSubject(cognitoSubject));
        ArtProcess savedArtProcess = artProcessRepository.save(artProcess);
        return artProcessMapper.toDto(savedArtProcess);
    }

    @Override
    @Transactional
    public ArtProcessResponseDto update(Long id, ArtProcessUpdateRequestDto dto,
                                                    String cognitoSubject) {
        ArtProcess artProcessFromDb = artProcessRepository.findById(id)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find art process by id: %s", id)));
        if (artProcessFromDb.getAuthor().getCognitoSubject().equals(cognitoSubject)) {
            ArtProcess artProcess = artProcessMapper.toModel(dto, artProcessFromDb);
            return artProcessMapper.toDto(artProcess);
        } else {
            throw new RuntimeException(
                    String.format("Author with cognitoSubject: %s is not allowed "
                            + "to update art process with id: %s", cognitoSubject, id));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ArtProcess artProcess = artProcessRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't  find art process by id = %s", id)));
        artProcessRepository.deleteById(id);
        String publicId = artProcess.getArtProcessImage().getImage().getPublicId();
        String delete = cloudinaryClient.delete(publicId);
        if (delete.equals("ok")) {
            log.info(String.format("Asset (art process image) with public id: %s was deleted from Cloudinary", publicId));
        } else {
            log.info(String.format("Asset (art process image) with public id: %s was NOT deleted from Cloudinary", publicId));
        }
    }

    @Override
    public ArtProcessResponseDto getById(Long id) {
       return artProcessRepository.findById(ArtProcessResponseDto.class, id).orElseThrow(
                () -> new AppEntityNotFoundException(
                String.format("Can't find art process by id: %s", id)));
    }

    @Override
    public List<ArtProcessResponseDto> getAllByAuthorPrettyId(String authorPrettyId) {
        return artProcessRepository.findByAuthorPrettyId(ArtProcessResponseDto.class,
                authorPrettyId);
    }

    @Override
    public List<ArtProcessResponseDto> getAllByAuthorCognitoSubject(String cognitoSubject) {
        return artProcessRepository.findByAuthorCognitoSubject(ArtProcessResponseDto.class,
                cognitoSubject);
    }

    @Override
    public FolderResponseDto createCloudinaryFolder(String cognitoSubject) {
        return cloudinaryFolderCreator.createForAtrProcess(cognitoSubject);
    }
}
