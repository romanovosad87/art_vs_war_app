package com.example.artvswar.controller;

import com.example.artvswar.dto.request.painting.PaintingCheckInputDto;
import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingMainPageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingParametersForSearchResponseDto;
import com.example.artvswar.dto.response.painting.PaintingProfileResponseDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.service.PaintingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paintings")
public class PaintingController {
    private static final String SUBJECT = "sub";
    private static final String ADDED_TO_DATABASE = "entityCreatedAt";
    private static final int DEFAULT_PAGE_SIZE = 6;
    public static final int DEFAULT_PAINTINGS_SIZE = 16;
    public static final int ALL_PAINTINGS_SIZE = 8;
    private final PaintingService paintingService;

    @GetMapping("/{id}")
    public ResponseEntity<PaintingResponseDto> getById(@PathVariable Long id) {
        PaintingResponseDto dto = paintingService.getDto(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/v2/{prettyId}")
    public ResponseEntity<PaintingResponseDto> getByPrettyId(@PathVariable String prettyId) {
        PaintingResponseDto dto = paintingService.getByPrettyId(prettyId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/profile/{prettyId}")
    public ResponseEntity<PaintingProfileResponseDto> getInProfileByPrettyId(
            @PathVariable String prettyId) {
        PaintingProfileResponseDto dto = paintingService.getForProfileByPrettyId(prettyId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/author/{authorPrettyId}")
    public ResponseEntity<Page<PaintingShortResponseDto>> getAllByAuthorPrettyId(
            @PathVariable String authorPrettyId,
            @PageableDefault(size = ALL_PAINTINGS_SIZE) Pageable pageable) {
        Page<PaintingShortResponseDto> allPaintings = paintingService
                .findAllByAuthorPrettyId(authorPrettyId, pageable);
        return new ResponseEntity<>(allPaintings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/author/all")
    public ResponseEntity<Page<PaintingShortResponseDto>> getAllByAuthorCognitoSubject(
            @AuthenticationPrincipal Jwt jwt,
            @SortDefault(sort = ADDED_TO_DATABASE, direction = Sort.Direction.DESC)
            @PageableDefault(size = ALL_PAINTINGS_SIZE) Pageable pageable) {
        String subject = jwt.getClaimAsString(SUBJECT);
        Page<PaintingShortResponseDto> allPaintings = paintingService
                .findAllByAuthorCognitoSubject(subject, pageable);
        return new ResponseEntity<>(allPaintings, HttpStatus.OK);
    }

    @GetMapping("/additional")
    public ResponseEntity<List<PaintingShortResponseDto>> getAdditionalPaintings(
            @RequestParam String paintingPrettyId,
            @RequestParam int size) {
        List<PaintingShortResponseDto> additionalPaintings
                = paintingService.getAdditionalPaintings(paintingPrettyId, size);
        return new ResponseEntity<>(additionalPaintings, HttpStatus.OK);
    }

    @GetMapping("/collection/{collectionPrettyId}")
    public ResponseEntity<Page<PaintingShortResponseDto>> getAllByCollection(
            @PathVariable String collectionPrettyId,
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<PaintingShortResponseDto> allPaintings
                = paintingService.findAllByCollectionPrettyId(collectionPrettyId, pageable);
        return new ResponseEntity<>(allPaintings, HttpStatus.OK);
    }

    @PostMapping("/checkInputAndGet")
    public FolderResponseDto checkInput(@RequestBody @Valid PaintingCheckInputDto dto,
                                        @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        return paintingService.createCloudinaryFolder(subject, dto.getTitle());
    }

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaintingResponseDto> create(
            @RequestBody @Valid PaintingCreateRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String authorSubject = jwt.getClaimAsString(SUBJECT);
        PaintingResponseDto responseDto = paintingService.save(dto, authorSubject);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{prettyId}")
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PaintingResponseDto> update(
            @PathVariable String prettyId,
            @RequestBody @Valid PaintingUpdateRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        PaintingResponseDto responseDto = paintingService.update(prettyId, dto, cognitoSubject);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getNumberOfPaintings() {
        return new ResponseEntity<>(paintingService.getNumberOfAllPaintings(),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PaintingShortResponseDto>> getAllByParamsReturnDto(
            @RequestParam Map<String, String> params,
            @PageableDefault(size = DEFAULT_PAINTINGS_SIZE) Pageable pageable) {
        Page<PaintingShortResponseDto> paintingsDto = paintingService.getAllByParamsReturnDto(params, pageable);
        return new ResponseEntity<>(paintingsDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/{prettyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String prettyId) {
        paintingService.deleteByPrettyId(prettyId);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/additionalImages/{prettyId}")
    public ResponseEntity<List<AdditionalImageResponseDto>> getAdditionalImagesOfPainting(
            @PathVariable String prettyId) {
        List<AdditionalImageResponseDto> dtos
                = paintingService.getAdditionalImagesByPrettyId(prettyId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/params")
    public ResponseEntity<PaintingParametersForSearchResponseDto> getActiveParams() {
        PaintingParametersForSearchResponseDto dto = paintingService.getDistinctParams();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<PaintingShortResponseDto>> getRecommendedPaintingsInShoppingCart(
            @RequestParam Set<String> prettyIds,
            @RequestParam int size) {
        List<PaintingShortResponseDto> recommendedPaintings = paintingService
                .getRecommendedPaintings(prettyIds, size);
        return new ResponseEntity<>(recommendedPaintings, HttpStatus.OK);
    }

    @GetMapping("/mainPage")
    public ResponseEntity<Map<Double, List<PaintingMainPageResponseDto>>> getPaintingsForMainPage() {
        Map<Double, List<PaintingMainPageResponseDto>> paintings = paintingService.getPaintingsForMainPage();
        return new ResponseEntity<>(paintings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/recentSelling")
    public ResponseEntity<Page<PaintingShortResponseDto>> getRecentSelling(
            @AuthenticationPrincipal Jwt jwt,
            @PageableDefault Pageable pageable) {
        String authorSubject = jwt.getClaimAsString(SUBJECT);
        Page<PaintingShortResponseDto> recentSelling = paintingService.findRecentSelling(authorSubject, pageable);
        return ResponseEntity.ok(recentSelling);
    }
}
