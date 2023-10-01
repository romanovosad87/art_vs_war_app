package com.example.artvswar.service;

import com.example.artvswar.dto.request.collection.CollectionCreateUpdateRequestDto;
import com.example.artvswar.dto.response.collection.CollectionShortResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CollectionService {
    CollectionShortResponseDto save(CollectionCreateUpdateRequestDto dto, String cognitoSubject);

    CollectionShortResponseDto update(CollectionCreateUpdateRequestDto dto, String prettyId, String cognitoSubject);
    CollectionShortResponseDto findByPrettyId(String collectionPrettyId);
    Page<CollectionShortResponseDto> getAllByAuthorByPrettyId(String prettyId, Pageable pageable);
    Page<CollectionShortResponseDto> getAllByAuthorByCognitoSubject(String cognitoSubject, Pageable pageable);
    void addPaintingsToCollection(String collectionPrettyId, List<Long> paintingIds);

    void removePaintingsFromCollection(String collectionPrettyId, List<Long> paintingIds);

    void delete(String collectionPrettyId);
}
