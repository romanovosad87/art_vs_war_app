package com.example.artvswar.repository.specification;

import com.example.artvswar.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AuthorSpecificationManager implements SpecificationManager<Author> {
    private final Map<String, SpecificationProvider<Author>> providersMap;

    @Autowired
    public AuthorSpecificationManager(List<SpecificationProvider<Author>> paintingSpecifications) {
        this.providersMap = paintingSpecifications.stream()
                .collect(Collectors.toMap(SpecificationProvider::getFilterKey, Function.identity()));
    }

    @Override
    public Specification<Author> get(String filterKey, String[] params) {
        if (!providersMap.containsKey(filterKey)) {
            throw new RuntimeException(
                    String.format("Key %s is not supported for data filtering", filterKey));
        }
        return providersMap.get(filterKey).getSpecification(params);
    }
}
