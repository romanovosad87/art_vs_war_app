package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.PaintingMapper;
import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.response.painting.PaintingDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.painting.PaintingRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.UrlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaintingServiceImpl implements PaintingService {
    private final PaintingRepository paintingRepository;
    private final PaintingMapper paintingMapper;
    private final UrlParser urlParser;
    private final AuthorService authorService;
    private final ImageService imageService;
    private final CloudinaryImageServiceImpl cloudinaryImageService;

    @Override
    @Transactional
    public Painting save(PaintingCreateRequestDto dto, String cognitoUsernane) {
        Painting painting = paintingMapper.toPaintingModel(dto);
        Author proxyAuthor = authorService.getReference(cognitoUsernane);
        painting.setAuthor(proxyAuthor);
        return paintingRepository.save(painting);
    }

    @Override
    @Transactional
    public Painting update(Painting painting) {
//        Painting paintingFromDb = paintingRepository.getByIdWithCustomQuery(painting.getId()).orElseThrow(
//                () -> new AppEntityNotFoundException(String.format("Can't find picture by id = %s",
//                        painting.getId())));
//        String imageFileNameFromDb = paintingFromDb.getImageFileName();
//        if (painting.getImageFileName() == null) {
//            painting.setImageFileName(imageFileNameFromDb);
//        } else {
//            if (imageFileNameFromDb != null) {
//                imageService.delete(imageFileNameFromDb);
//            }
//        }
//        if (painting.getDescription() == null) {
//            painting.setDescription(paintingFromDb.getDescription());
//        }
        return paintingRepository.save(painting);
    }

    @Override
    public Painting get(Long id) {
        return paintingRepository.getByIdWithCustomQuery(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find picture by id = %s", id)));
    }

    @Override
    public PaintingResponseDto getDto(Long id) {
        Painting painting = paintingRepository.getByIdWithCustomQuery(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find picture by id = %s", id)));
        return paintingMapper.toPaintingResponseDto(painting);
    }

    @Override
    @Transactional
    public void delete(Painting painting) {
        paintingRepository.delete(painting);
//        imageService.delete(painting.getImage());
    }

    @Override
    public long getNumberOfAllPaintings() {
        return paintingRepository.count();
    }

    public BigDecimal getMaxPrice() {
        return paintingRepository.getMaxPrice().orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getMinPrice() {
        return paintingRepository.getMinPrice().orElse(BigDecimal.ZERO);
    }

    @Override
    public Integer getMaxHeight() {
        return paintingRepository.getMaxHeight().orElse(0);
    }

    @Override
    public Integer getMaxWidth() {
        return paintingRepository.getMaxWidth().orElse(0);
    }

    @Override
    public Page<PaintingDto> getAllByParamsReturnDto(Map<String, String> params,
                                                     Pageable pageable) {
        Specification<Painting> specification = urlParser.getPaintingSpecification(params);
        return paintingRepository.getAllDtosBySpecification(specification, pageable);
    }
}
