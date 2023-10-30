package com.example.artvswar.service;

import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingMainPageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingParametersForSearchResponseDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.enumModel.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PaintingService {
    PaintingResponseDto save(PaintingCreateRequestDto dto, String cognitoSubject);

    PaintingResponseDto update(String prettyId, PaintingUpdateRequestDto dto, String cognitoSubject);

    Painting get(Long id);

    Painting getReference(Long id);
    Page<PaintingShortResponseDto> getAll(Map<String, String> params, Pageable pageable);

    PaintingResponseDto getDto(Long id);

    PaintingResponseDto getByPrettyId(String prettyId);

    void deleteByPrettyId(String prettyId);

    Page<PaintingShortResponseDto> getAllByParamsReturnDto(Map<String, String> params, Pageable pageable);

    long getNumberOfAllPaintings();
    Page<PaintingShortResponseDto> findAllByAuthorPrettyId(String authorPrettyId,
                                                           Pageable pageable);
    Page<PaintingShortResponseDto> findAllByAuthorCognitoSubject(String cognitoSubject,
                                                           Pageable pageable);
    List<PaintingShortResponseDto> getAdditionalPaintings(String paintingPrettyId, int size);
    List<PaintingShortResponseDto> getRecommendedPaintings(Set<String> paintingPrettyIds, int limit);
    Page<PaintingShortResponseDto> findAllByCollectionPrettyId(String collectionPrettyId,
                                                               Pageable pageable);
    FolderResponseDto createCloudinaryFolder(String cognitoSubject, String title);

    String createPrettyId(String title);

    List<AdditionalImageResponseDto> getAdditionalImagesByPrettyId(String prettyId);

    PaintingParametersForSearchResponseDto getDistinctParams();

    Map<Double, List<PaintingMainPageResponseDto>> getPaintingsForMainPage();

    void changePaymentStatus(Painting painting, PaymentStatus paymentStatus);

    Page<PaintingShortResponseDto> findRecentSelling(String authorSubject, Pageable pageable);
}
