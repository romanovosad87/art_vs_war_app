package com.example.artvswar.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResponseCreator {

    public <T, V> ResponseEntity<Map<String, Object>> createResponse(List<T> returnElements,
                                                                     Page<V> pageElement,
                                                                     String returnElementsName) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(returnElementsName, returnElements);
        response.put("page", Map.of("pageSize", pageElement.getSize(),
                "totalElements", pageElement.getTotalElements(),
                "totalPages", pageElement.getTotalPages(),
                "pageNumber", pageElement.getNumber()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
