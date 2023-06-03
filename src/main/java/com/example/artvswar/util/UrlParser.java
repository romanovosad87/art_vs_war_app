package com.example.artvswar.util;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.specification.PaintingSpecificationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UrlParser {
    private final static int DEFAULT_IMAGES_QUANTITY = 12;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static String PAGE = "page";
    private final static String PAGE_SIZE = "size";
    private final static String SORT_BY = "sort";
    private final static String COMMA = ",";
    private final UrlSortParser urlSortParser;
    private final PaintingSpecificationManager paintingSpecificationManager;

    public PageRequest getPageRequest(Map<String, String> params) {
        Sort sort = Sort.unsorted();
        int page = DEFAULT_PAGE_NUMBER;
        int pageSize = DEFAULT_IMAGES_QUANTITY;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            switch (entry.getKey()) {
                case PAGE:
                    page = Integer.parseInt(entry.getValue());
                    break;
                case PAGE_SIZE:
                    pageSize = Integer.parseInt(entry.getValue());
                    break;
                case SORT_BY:
                    sort = urlSortParser.getSort(entry.getValue());
                    break;
            }
        }
        return PageRequest.of(page, pageSize, sort);
    }

    public Specification<Painting> getPaintingSpecification(Map<String, String> params) {
        Specification<Painting> specification = null;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().equals(PAGE) && !entry.getKey().equals(PAGE_SIZE)
                    && !entry.getKey().equals(SORT_BY)) {
                Specification<Painting> spec = paintingSpecificationManager.get(entry.getKey(),
                        entry.getValue().split(COMMA));
                specification = specification == null
                        ? Specification.where(spec)
                        : specification.and(spec);
            }
        }
        return specification;
    }
}
