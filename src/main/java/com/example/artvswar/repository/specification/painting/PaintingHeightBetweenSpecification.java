package com.example.artvswar.repository.specification.painting;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaintingHeightBetweenSpecification implements SpecificationProvider<Painting> {
    private static final String FILTER_KEY = "heightBetween";
    private static final String FIELD_NAME = "height";

    @Override
    public Specification<Painting> getSpecification(String[] fromToHeight) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(FIELD_NAME),
                        Integer.parseInt(fromToHeight[0]),
                        Integer.parseInt(fromToHeight[1])));
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
