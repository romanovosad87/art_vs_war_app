package com.example.artvswar.util;

import com.example.artvswar.dto.response.EntitiesPageResponse;
import com.example.artvswar.dto.response.page.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class EntitiesResponseCreator {

    public <T, V> ResponseEntity<EntitiesPageResponse<T>> createResponse(List<T> returnElements,
                                                                         Page<V> pageElement) {

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageSize(pageElement.getSize());
        pageResponse.setTotalElements(pageElement.getTotalElements());
        pageResponse.setTotalPages(pageElement.getTotalPages());
        pageResponse.setPageNumber(pageElement.getNumber());

        EntitiesPageResponse<T> entitiesPageResponse = new EntitiesPageResponse<>();
        entitiesPageResponse.setEntities(returnElements);
        entitiesPageResponse.setPage(pageResponse);

        return new ResponseEntity<>(entitiesPageResponse, HttpStatus.OK);
    }
}
