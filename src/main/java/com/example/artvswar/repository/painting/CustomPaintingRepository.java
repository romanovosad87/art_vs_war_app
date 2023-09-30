package com.example.artvswar.repository.painting;

import com.example.artvswar.dto.response.MainPageDataResponseDto;
import com.example.artvswar.dto.response.painting.PaintingParametersForSearchResponseDto;
import com.example.artvswar.dto.response.painting.PaintingDto;
import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.model.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Set;

public interface CustomPaintingRepository {

    Page<PaintingDto> getAllDtosBySpecification(Specification<Painting> specification, Pageable pageable);
    Page<PaintingShortResponseDto> getAllShortDtosBySpecification(Specification<Painting> specification, Pageable pageable);

    Page<PaintingShortResponseDto> findAllByAuthorPrettyId(String authorPrettyId,
                                                           Pageable pageable);

    List<PaintingShortResponseDto> getAdditionalPaintings(String paintingPrettyId, String authorPrettyId, int limit);
    List<PaintingShortResponseDto> getRecommendedPaintings(Set<String> paintingPrettyIds, int limit);

    PaintingParametersForSearchResponseDto getDistinctParameters();

    Page<PaintingShortResponseDto> findAllByCollectionPrettyId(String collectionPrettyId,
                                            Pageable pageable);

    MainPageDataResponseDto getDataForMainPage();
}
