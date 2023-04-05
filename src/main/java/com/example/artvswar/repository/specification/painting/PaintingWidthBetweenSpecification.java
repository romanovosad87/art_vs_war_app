package com.example.artvswar.repository.specification.painting;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaintingWidthBetweenSpecification implements SpecificationProvider<Painting> {
    private static final String FILTER_KEY = "widthBetween";
    private static final String FIELD_NAME = "width";

    @Override
    public Specification<Painting> getSpecification(String[] fromToWidth) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(FIELD_NAME),
                        Integer.parseInt(fromToWidth[0]),
                        Integer.parseInt(fromToWidth[1])));
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
