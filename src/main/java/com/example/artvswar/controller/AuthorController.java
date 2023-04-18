package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.AuthorMapper;
import com.example.artvswar.dto.request.AuthorRequestDto;
import com.example.artvswar.dto.response.AuthorResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.service.AuthorService;
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
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/authors")
public class AuthorController {
    private final static String IMAGES_QUANTITY = "12";
    private final static String PAGE_NUMBER = "0";
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final ResponseCreator creator;


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAuthors(
            @RequestParam(defaultValue = PAGE_NUMBER) Integer page,
            @RequestParam(defaultValue = IMAGES_QUANTITY) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        Page<Author> pages = authorService.getAll(pageRequest);
        List<AuthorResponseDto> allListOfAuthors = pages.stream()
                .map(authorMapper::toAuthorResponseDto)
                .collect(Collectors.toList());

        return creator.createResponse(allListOfAuthors, pages, "authors");
    }

    @GetMapping("/{id}")
    public AuthorResponseDto getById(@PathVariable String id) {
        Author author = authorService.get(id);
        return authorMapper.toAuthorResponseDto(author);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody AuthorRequestDto dto) {
        Author author = authorMapper.toAuthorModel(dto);
        authorMapper.toAuthorResponseDto(authorService.save(author));
    }

    @PutMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public AuthorResponseDto update(@RequestBody @Valid AuthorRequestDto dto,
                                    @AuthenticationPrincipal Jwt jwt) {
        Author author = authorMapper.toAuthorModel(dto);
        author.setId(jwt.getClaimAsString("username"));
        return authorMapper.toAuthorResponseDto(authorService.save(author));
    }

    @GetMapping("/count")
    public ResponseEntity<Object> getNumberOfAllAuthors() {
        return new ResponseEntity<>(authorService.getNumberOfAllAuthors(),
                HttpStatus.OK);
    }
}
