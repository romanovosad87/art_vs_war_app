package com.example.artvswar.service.impl;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.PaintingRepository;
import com.example.artvswar.repository.specification.PaintingSpecificationManager;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.UrlSortParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaintingServiceImpl implements PaintingService {
    private final static int DEFAULT_IMAGES_QUANTITY = 12;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final PaintingRepository paintingRepository;
    private final PaintingSpecificationManager paintingSpecificationManager;
    private final UrlSortParser urlSortParser;

    @Override
    @Transactional
    public Painting save(Painting painting) {
        return paintingRepository.save(painting);
    }

    @Override
    public Painting get(Long id) {
        return paintingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find picture by %s", id)));
    }

    @Override
    public Page<Painting> getAll(PageRequest pageRequest) {
        return paintingRepository.findAll(pageRequest);
    }

    @Override
    public long getNumberOfAllPaintings() {
        return paintingRepository.count();
    }

    @Override
    public Page<Painting> getAllByParams(Map<String, String> params) {
        Specification<Painting> specification = null;
        Sort sort = Sort.unsorted();
        int page = DEFAULT_PAGE_NUMBER;
        int pageSize = DEFAULT_IMAGES_QUANTITY;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            switch (entry.getKey()) {
                case "page":
                    page = Integer.parseInt(entry.getValue());
                    break;
                case "pageSize":
                    pageSize = Integer.parseInt(entry.getValue());
                    break;
                case "sortBy":
                    sort = urlSortParser.getSort(entry.getValue());
                    break;
                default:
                    Specification<Painting> spec = paintingSpecificationManager.get(entry.getKey(),
                            entry.getValue().split(","));
                    specification = specification == null
                            ? Specification.where(spec)
                            : specification.and(spec);
                    break;
            }
        }
        PageRequest pageRequest = PageRequest.of(page, pageSize, sort);
        return paintingRepository.findAll(specification, pageRequest);
    }

    @Override
    public Page<Painting> getAllPaintingsByAuthorId(String id, PageRequest pageRequest) {
        return paintingRepository.findPaintingByAuthorId(id, pageRequest);
    }
}
