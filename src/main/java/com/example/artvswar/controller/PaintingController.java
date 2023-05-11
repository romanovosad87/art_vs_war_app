package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.PaintingMapper;
import com.example.artvswar.dto.request.PaintingRequestDto;
import com.example.artvswar.dto.response.EntitiesPageResponse;
import com.example.artvswar.dto.response.PaintingResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.EntitiesResponseCreator;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/paintings")
public class PaintingController {
    private final PaintingService paintingService;
    private final PaintingMapper paintingMapper;
    private final EntitiesResponseCreator creator;
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<EntitiesPageResponse<PaintingResponseDto>> getAll(
            @RequestParam Map<String, String> params) {
        Page<Painting> allPaintings = paintingService.getAll(params);
        List<PaintingResponseDto> allListOfPaintings = allPaintings.stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());
        Collections.shuffle(allListOfPaintings);

        return creator.createResponse(allListOfPaintings, allPaintings);
    }

    @GetMapping("/by-author/{authorId}")
    public ResponseEntity<EntitiesPageResponse<PaintingResponseDto>> getAllByAuthorId(@PathVariable String authorId,
                                                                                      @RequestParam Map<String, String> params) {
        Page<Painting> pages = paintingService.getAllPaintingsByAuthorId(authorId, params);
        List<PaintingResponseDto> allListByAuthor = pages.stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());

        return creator.createResponse(allListByAuthor, pages);
    }

    @GetMapping("/{id}")
    public PaintingResponseDto getById(@PathVariable Long id) {
        Painting painting = paintingService.get(id);
        return paintingMapper.toPaintingResponseDto(painting);
    }

    @PostMapping
//    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public PaintingResponseDto create(@RequestBody @Valid PaintingRequestDto dto,
                                      @AuthenticationPrincipal Jwt jwt) {
        Painting painting = paintingMapper.toPaintingModel(dto);
        String authorUsername = jwt.getClaimAsString("username");
        Author author = authorService.get(authorUsername);
        author.addPainting(painting);
        return paintingMapper.toPaintingResponseDto(paintingService.save(painting));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.OK)
    public PaintingResponseDto update(@PathVariable Long id,
                                      @RequestBody @Valid PaintingRequestDto dto,
                                      @AuthenticationPrincipal Jwt jwt) {
        Painting painting = paintingMapper.toPaintingModel(dto);
        painting.setId(id);
        String authorUsername = jwt.getClaimAsString("username");
        Author author = authorService.get(authorUsername);
//        author.addPainting(painting);
        return paintingMapper.toPaintingResponseDto(paintingService.update(painting));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getNumberOfPaintings() {
        return new ResponseEntity<>(paintingService.getNumberOfAllPaintings(),
                HttpStatus.OK);
    }

    @GetMapping("/maxPrice")
    public ResponseEntity<BigDecimal> getMaxPrice() {
        return new ResponseEntity<>(paintingService.getMaxPrice(), HttpStatus.OK);
    }

    @GetMapping("/minPrice")
    public ResponseEntity<BigDecimal> getMinPrice() {
       return new ResponseEntity<>(paintingService.getMinPrice(), HttpStatus.OK);
    }

    @GetMapping("/maxHeight")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> getMaxHeight() {
        return new ResponseEntity<>(paintingService.getMaxHeight(), HttpStatus.OK);
    }

    @GetMapping("/maxWidth")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> getMaxWidth() {
        return new ResponseEntity<>(paintingService.getMaxWidth(), HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<EntitiesPageResponse<PaintingResponseDto>> getAllByParams(
            @RequestParam Map<String, String> params) {
        Page<Painting> pages = paintingService.getAllByParams(params);

        List<PaintingResponseDto> allListOfPaintings = pages.stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());

        return creator.createResponse(allListOfPaintings, pages);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal Jwt jwt) {
        String authorUsername = jwt.getClaimAsString("username");
        Author author = authorService.get(authorUsername);
        Painting painting = paintingService.get(id);
        author.remove(painting);
        paintingService.delete(painting);
    }
}
