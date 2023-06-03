package com.example.artvswar.repository.painting;

import com.example.artvswar.dto.response.painting.PaintingDto;
import com.example.artvswar.model.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;

public interface CustomPaintingRepository {

    Page<PaintingDto> getAllDtos(Pageable pageable);
    Page<PaintingDto> getAllDtosBySpecification(Specification<Painting> specification, Pageable pageable);
    Optional<Painting> getByIdWithCustomQuery(Long id);
}
