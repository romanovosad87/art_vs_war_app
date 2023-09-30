package com.example.artvswar.repository.specification.painting;

import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaintingTitleSpecification implements SpecificationProvider<Painting> {
    private static final String FILTER_KEY = "query";
    private static final String FIELD_NAME = "title";
    @Override
    public Specification<Painting> getSpecification(String[] words) {
       String word = words[0].trim().toUpperCase().replaceAll("%", "");
        return ((root, query, cb) ->
                cb.like(cb.upper(root.get(FIELD_NAME)), "%" + word + "%"));
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
