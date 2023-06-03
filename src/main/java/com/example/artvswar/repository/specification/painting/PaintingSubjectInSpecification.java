package com.example.artvswar.repository.specification.painting;

import com.example.artvswar.model.Painting;
import com.example.artvswar.model.Style;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

@Component
public class PaintingSubjectInSpecification implements SpecificationProvider<Painting> {
    private static final String SUBJECTS = "subjects";
    private static final String FILTER_KEY = "subjectIn";
    private static final String FIELD_NAME = "name";
    @Override
    public Specification<Painting> getSpecification(String[] subjects) {
        return ((root, query, criteriaBuilder) -> {
            Join<Painting, Style> join = root.join(SUBJECTS, JoinType.INNER);
            CriteriaBuilder.In<String> predicate = criteriaBuilder.in(join.get(FIELD_NAME));
            for (String value : subjects) {
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
