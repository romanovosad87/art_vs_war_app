package com.example.artvswar.repository.specification.painting;

import com.example.artvswar.model.Painting;
import com.example.artvswar.model.enumModel.PaymentStatus;
import com.example.artvswar.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaintingOnlyAvailableSpecification implements SpecificationProvider<Painting> {
    private static final String FILTER_KEY = "paymentStatus";
    private static final String FIELD_NAME = "paymentStatus";
    @Override
    public Specification<Painting> getSpecification(String[] params) {
        String paymentStatus = params[0].toUpperCase();
        return ((root, query, cb) ->
                cb.equal(root.get(FIELD_NAME), PaymentStatus.valueOf(paymentStatus)));
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
