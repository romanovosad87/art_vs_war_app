package com.example.artvswar.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UrlSortParser {
    private static final int DIRECTION_INDEX = 1;
    private static final int FIELD_INDEX = 0;
    private static final String COLON_SEPARATOR = ":";
    private static final String SEMICOLON_SEPARATOR = ";";

    public Sort getSort(String sortBy) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortBy.contains(COLON_SEPARATOR)) {
            String[] sortingFields = sortBy.split(SEMICOLON_SEPARATOR);
            for (String field : sortingFields) {
                Sort.Order order;
                if (field.contains(COLON_SEPARATOR)) {
                    String[] fieldsAndDirections = field.split(COLON_SEPARATOR);
                    order = new Sort.Order(Sort.Direction.valueOf(fieldsAndDirections[DIRECTION_INDEX]),
                            fieldsAndDirections[FIELD_INDEX]);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, field);
                }
                orders.add(order);
            }
        } else {
            Sort.Order order = new Sort.Order(Sort.Direction.DESC, sortBy);
            orders.add(order);
        }
        return Sort.by(orders);
    }
}
