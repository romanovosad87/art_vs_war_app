package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.PaintingMapper;
import com.example.artvswar.dto.request.PaintingRequestDto;
import com.example.artvswar.dto.response.PaintingResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PaintingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

//@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/paintings")
public class PaintingController {

    private final PaintingService paintingService;
    private final PaintingMapper paintingMapper;
   // private final JWTExtractor jwtExtractor;
    private final AuthorService authorService;

    @GetMapping
    public List<PaintingResponseDto> getAll(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("entityCreatedAt").descending());
        return paintingService.getAll(pageRequest).stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PaintingResponseDto getById(@PathVariable Long id) {
        Painting painting = paintingService.get(id);
        return paintingMapper.toPaintingResponseDto(painting);
    }

    @PostMapping
    public PaintingResponseDto create(@RequestBody @Valid PaintingRequestDto dto,
                                      HttpServletRequest httpServletRequest) {
        Painting painting = paintingMapper.toPaintingModel(dto);
//        String userEmail = jwtExtractor.getUsername(httpServletRequest);
//        Author author = authorService.getAuthorByEmail(userEmail);
//        painting.setAuthor(author);
        return paintingMapper.toPaintingResponseDto(paintingService.save(painting));
    }

    @GetMapping("/count")
    public ResponseEntity<Object> getNumberOfPaintings() {
        return new ResponseEntity<>(paintingService.getNumberOfAllPaintings(),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<PaintingResponseDto> getAllByParams(@RequestParam Map<String, String> params) {

        return paintingService.getAllByParams(params).stream()
                .map(paintingMapper::toPaintingResponseDto)
                .collect(Collectors.toList());

    }
}
