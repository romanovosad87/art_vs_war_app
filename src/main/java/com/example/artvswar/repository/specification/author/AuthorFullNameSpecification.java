package com.example.artvswar.repository.specification.author;

import com.example.artvswar.model.Author;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorFullNameSpecification implements SpecificationProvider<Author> {
    private static final String FILTER_KEY = "query";
    private static final String FIELD_NAME = "fullName";
    @Override
    public Specification<Author> getSpecification(String[] words) {
        String word = words[0].trim().toUpperCase().replaceAll("%", "");
        return ((root, query, cb) ->
                cb.like(cb.upper(root.get(FIELD_NAME)), "%" + word + "%"));
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
