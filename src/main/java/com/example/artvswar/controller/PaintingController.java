package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.PaintingMapper;
import com.example.artvswar.dto.request.PaintingRequestDto;
import com.example.artvswar.dto.response.PaintingResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.ResponseCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paintings")
public class PaintingController {
    private final static String IMAGES_QUANTITY = "12";
    private final static String PAGE_NUMBER = "0";
    private final PaintingService paintingService;
    private final PaintingMapper paintingMapper;
    private final ResponseCreator creator;
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(defaultValue = PAGE_NUMBER) Integer page,
            @RequestParam(defaultValue = IMAGES_QUANTITY) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("entityCreatedAt").descending());
        Page<Painting> allPaintings = paintingService.getAll(pageRequest);
        List<PaintingResponseDto> allListOfPaintings = allPaintings.stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());

        return creator.createResponse(allListOfPaintings, allPaintings, "paintings");
    }

    @GetMapping("/by-author")
    public ResponseEntity<Map<String, Object>> getAllByAuthorId(@RequestParam String authorId,
                                                                 @RequestParam(defaultValue = PAGE_NUMBER) Integer page,
                                                                 @RequestParam(defaultValue = IMAGES_QUANTITY) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("entityCreatedAt").descending());
        Page<Painting> pages = paintingService.getAllPaintingsByAuthorId(authorId, pageRequest);
        List<PaintingResponseDto> allListByAuthor = pages.stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());

        return creator.createResponse(allListByAuthor, pages, "paintings");
    }

    @GetMapping("/{id}")
    public PaintingResponseDto getById(@PathVariable Long id) {
        Painting painting = paintingService.get(id);
        return paintingMapper.toPaintingResponseDto(painting);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PaintingResponseDto create(@RequestBody @Valid PaintingRequestDto dto,
                                      @AuthenticationPrincipal Jwt jwt) {
        Painting painting = paintingMapper.toPaintingModel(dto);
        String authorUsername = jwt.getClaimAsString("username");
        Author author = authorService.get(authorUsername);
        author.addPainting(painting);
        return paintingMapper.toPaintingResponseDto(paintingService.save(painting));
    }

    @GetMapping("/count")
    public ResponseEntity<Object> getNumberOfPaintings() {
        return new ResponseEntity<>(paintingService.getNumberOfAllPaintings(),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getAllByParams(
            @RequestParam Map<String, String> params) {
        Page<Painting> pages = paintingService.getAllByParams(params);

        List<PaintingResponseDto> allListOfPaintings = pages.stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());

        return creator.createResponse(allListOfPaintings, pages, "paintings");
    }
}
