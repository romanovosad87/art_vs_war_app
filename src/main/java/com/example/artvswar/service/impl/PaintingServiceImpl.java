package com.example.artvswar.service.impl;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.PaintingRepository;
import com.example.artvswar.repository.specification.PaintingSpecificationManager;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.UrlSortParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class PaintingServiceImpl implements PaintingService {

    private final PaintingRepository paintingRepository;
    private final PaintingSpecificationManager paintingSpecificationManager;
    private final UrlSortParser urlSortParser;

    @Override
    public Painting save(Painting painting) {
        return paintingRepository.save(painting);
    }

    @Override
    public Painting get(Long id) {
        return paintingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find picture by %s", id)));
    }

    @Override
    public List<Painting> getAll(PageRequest pageRequest) {
        return paintingRepository.findAll(pageRequest).toList();
    }

    @Override
    public long getNumberOfAllPaintings() {
        return paintingRepository.count();
    }

    @Override
    public List<Painting> getAllByParams(Map<String, String> params) {
        Specification<Painting> specification = null;
        Sort sort = Sort.unsorted();
        int page = 0;
        int pageSize = 10;
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
        return paintingRepository.findAll(specification, pageRequest).toList();
    }
}
