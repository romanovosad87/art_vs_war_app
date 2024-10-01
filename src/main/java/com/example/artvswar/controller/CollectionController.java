package com.example.artvswar.controller;

import com.example.artvswar.dto.request.collection.CollectionAddRemovePaintingsRequestDto;
import com.example.artvswar.dto.request.collection.CollectionCreateUpdateRequestDto;
import com.example.artvswar.dto.response.collection.CollectionShortResponseDto;
import com.example.artvswar.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("collections")
public class CollectionController {
    private static final String SUBJECT = "sub";
    private final CollectionService collectionService;

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionShortResponseDto create(@RequestBody @Valid CollectionCreateUpdateRequestDto dto,
                                              @AuthenticationPrincipal Jwt jwt) {
        String authorSubject = jwt.getClaimAsString(SUBJECT);
        return collectionService.save(dto, authorSubject);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("/{prettyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CollectionShortResponseDto update(@PathVariable String prettyId,
                                             @RequestBody @Valid CollectionCreateUpdateRequestDto dto,
                                             @AuthenticationPrincipal Jwt jwt) {
        String authorSubject = jwt.getClaimAsString(SUBJECT);
        return collectionService.update(dto, prettyId, authorSubject);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/add/{collectionPrettyId}")
    @ResponseStatus(HttpStatus.OK)
    public void addPaintings(@PathVariable String collectionPrettyId,
                             @RequestBody @Valid CollectionAddRemovePaintingsRequestDto dto) {
        collectionService.addPaintingsToCollection(collectionPrettyId, dto.getPaintingsIds());
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/remove/{collectionPrettyId}")
    @ResponseStatus(HttpStatus.OK)
    public void removePaintings(@PathVariable String collectionPrettyId,
                             @RequestBody @Valid CollectionAddRemovePaintingsRequestDto dto) {
        collectionService.removePaintingsFromCollection(collectionPrettyId, dto.getPaintingsIds());
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @GetMapping("/all")
    public ResponseEntity<Page<CollectionShortResponseDto>> getAllByAuthorWithToken(
             @PageableDefault(size = 12) Pageable pageable,
             @AuthenticationPrincipal Jwt jwt) {
        String authorSubject = jwt.getClaimAsString(SUBJECT);
        Page<CollectionShortResponseDto> allByAuthor
                = collectionService.getAllByAuthorByCognitoSubject(authorSubject, pageable);
        return new ResponseEntity<>(allByAuthor, HttpStatus.OK);
    }

    @GetMapping("/all/{authorPrettyId}")
    public ResponseEntity<Page<CollectionShortResponseDto>> getAllByAuthorPrettyId(
            @PathVariable String authorPrettyId, @PageableDefault(size = 12) Pageable pageable) {
        Page<CollectionShortResponseDto> allByAuthor
                = collectionService.getAllByAuthorByPrettyId(authorPrettyId, pageable);
        return new ResponseEntity<>(allByAuthor, HttpStatus.OK);
    }

    @GetMapping("/{collectionPrettyId}")
    public CollectionShortResponseDto getByPrettyId(@PathVariable String collectionPrettyId) {
        return collectionService.findByPrettyId(collectionPrettyId);
    }


    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/{collectionPrettyId}")
    public void deleteByCollectionPrettyId(@PathVariable String collectionPrettyId) {
        collectionService.delete(collectionPrettyId);
    }
}
