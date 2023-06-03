package com.example.artvswar.controller;

import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.response.painting.PaintingCreatedResponseDto;
import com.example.artvswar.dto.response.painting.PaintingDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PaintingService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Map;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paintings")
public class PaintingController {
    private final PaintingService paintingService;
    private final AuthorService authorService;

    @GetMapping("/{id}")
    public ResponseEntity<PaintingResponseDto> getById(@PathVariable Long id) {
        PaintingResponseDto dto = paintingService.getDto(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaintingCreatedResponseDto> create(@RequestBody @Valid PaintingCreateRequestDto dto,
                                      @AuthenticationPrincipal Jwt jwt) {
        String authorUsername = jwt.getClaimAsString("username");
        Painting savedPainting = paintingService.save(dto, authorUsername);
        PaintingCreatedResponseDto createdResponseDto
                = new PaintingCreatedResponseDto(savedPainting.getId());
        return new ResponseEntity<>(createdResponseDto, HttpStatus.CREATED);
    }

//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('AUTHOR')")
//    @ResponseStatus(HttpStatus.OK)
//    public PaintingResponseDto update(@PathVariable Long id,
//                                      @RequestBody @Valid PaintingUpdateRequestDto dto,
//                                      @AuthenticationPrincipal Jwt jwt) {
//        Painting painting = paintingMapper.toPaintingModel(dto);
//        painting.setId(id);
//        String authorUsername = jwt.getClaimAsString("username");
//        Author author = authorService.getAuthorByCognitoUsername(authorUsername);
////        author.addPainting(painting);
//        return paintingMapper.toPaintingResponseDto(paintingService.update(painting));
//    }

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
    public ResponseEntity<Page<PaintingDto>> getAllByParamsReturnDto(
            @RequestParam Map<String, String> params, @PageableDefault(size = 12) Pageable pageable) {
        Page<PaintingDto> paintingsDto = paintingService.getAllByParamsReturnDto(params, pageable);
        return new ResponseEntity<>(paintingsDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal Jwt jwt) {
        String authorUsername = jwt.getClaimAsString("username");
        Author author = authorService.getAuthorByCognitoUsername(authorUsername);
        Painting painting = paintingService.get(id);
//        author.remove(painting);
        paintingService.delete(painting);
    }
}
