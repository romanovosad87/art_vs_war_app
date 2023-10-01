package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.collection.CollectionCreateUpdateRequestDto;
import com.example.artvswar.dto.response.collection.CollectionShortResponseDto;
import com.example.artvswar.model.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollectionMapper {

    public Collection toModel(CollectionCreateUpdateRequestDto dto) {
        Collection collection = new Collection();
        collection.setTitle(dto.getTitle().trim());
        collection.setDescription(dto.getDescription().trim());
        return collection;
    }

    public Collection toModel(Collection collection, CollectionCreateUpdateRequestDto dto) {
        collection.setTitle(dto.getTitle().trim());
        collection.setDescription(dto.getDescription().trim());
        return collection;
    }

    public CollectionShortResponseDto toDto(Collection collection) {
        return new CollectionShortResponseDto(
                collection.getId(),
                collection.getPrettyId(),
                collection.getTitle(),
                collection.getDescription(),
                collection.getAuthor().getFullName(),
                collection.getAuthor().getPrettyId());
    }
}
