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
public class PaintingAuthorInSpecification implements SpecificationProvider<Painting> {
    private static final String AUTHOR = "author";
    private static final String FILTER_KEY = "authorIn";
    private static final String FIELD_NAME = "cognitoUsername";
    @Override
    public Specification<Painting> getSpecification(String[] authors) {
        return ((root, query, criteriaBuilder) -> {
            Join<Painting, Style> join = root.join(AUTHOR, JoinType.INNER);
            CriteriaBuilder.In<String> predicate = criteriaBuilder.in(join.get(FIELD_NAME));
            for (String value : authors) {
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
