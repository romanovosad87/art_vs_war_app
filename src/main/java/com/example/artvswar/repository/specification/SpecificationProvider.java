package com.example.artvswar.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T>  {
    Specification<T> getSpecification(String[] params);

    String getFilterKey();
}
