package com.example.artvswar.repository.specification.painting;

import com.example.artvswar.model.Painting;
import com.example.artvswar.model.Style;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Component
public class PaintingSupportSpecification implements SpecificationProvider<Painting> {
    private static final String SUPPORTS = "supports";
    private static final String FILTER_KEY = "supportIn";
    private static final String FIELD_NAME = "name";
    @Override
    public Specification<Painting> getSpecification(String[] supports) {
        return ((root, query, criteriaBuilder) -> {
            Join<Painting, Style> join = root.join(SUPPORTS, JoinType.INNER);
            CriteriaBuilder.In<String> predicate = criteriaBuilder.in(join.get(FIELD_NAME));
            for (String value : supports) {
                predicate.value(value);
            }
            return criteriaBuilder.and(predicate, predicate);
        });
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
