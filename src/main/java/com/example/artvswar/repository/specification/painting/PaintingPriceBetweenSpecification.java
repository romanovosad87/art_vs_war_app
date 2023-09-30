package com.example.artvswar.repository.specification.painting;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class PaintingPriceBetweenSpecification implements SpecificationProvider<Painting> {
    private static final String FILTER_KEY = "priceBetween";
    private static final String FIELD_NAME = "price";
    @Override
    public Specification<Painting> getSpecification(String[] fromToPrice) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(FIELD_NAME),
                        BigDecimal.valueOf(Long.parseLong(fromToPrice[0])),
                        BigDecimal.valueOf(Long.parseLong(fromToPrice[1]))));
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
