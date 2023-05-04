package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.PaintingRepository;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.UrlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final UrlParser urlParser;
    private final ImageService imageService;

    @Override
    @Transactional
    public Painting save(Painting painting) {
        return paintingRepository.save(painting);
    }

    @Override
    @Transactional
    public Painting update(Painting painting) {
        Painting paintingFromDb = paintingRepository.findById(painting.getId()).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find picture by id = %s",
                        painting.getId())));
        String imageFileNameFromDb = paintingFromDb.getImageFileName();
        if (painting.getImageFileName() == null) {
            painting.setImageFileName(imageFileNameFromDb);
        } else {
            if (imageFileNameFromDb != null) {
                imageService.delete(imageFileNameFromDb);
            }
        }
        if (painting.getDescription() == null) {
            painting.setDescription(paintingFromDb.getDescription());
        }
        return paintingRepository.save(painting);
    }

    @Override
    public Painting get(Long id) {
        return paintingRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find picture by id = %s", id)));
    }

    @Override
    @Transactional
    public void delete(Painting painting) {
        paintingRepository.delete(painting);
        imageService.delete(painting.getImageFileName());
    }

    @Override
    public Page<Painting> getAll(Map<String, String> params) {
        PageRequest pageRequest = urlParser.getPageRequest(params);
        return paintingRepository.findAll(pageRequest);
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
    public Page<Painting> getAllByParams(Map<String, String> params) {
        PageRequest pageRequest = urlParser.getPageRequest(params);
        Specification<Painting> specification = urlParser.getPaintingSpecification(params);
        return paintingRepository.findAll(specification, pageRequest);
    }

    @Override
    public Page<Painting> getAllPaintingsByAuthorId(String id, Map<String, String> params) {
        PageRequest pageRequest = urlParser.getPageRequest(params);
        return paintingRepository.findPaintingByAuthorId(id, pageRequest);
    }
}
