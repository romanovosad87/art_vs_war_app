package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.AuthorMapper;
import com.example.artvswar.dto.request.AuthorRequestDto;
import com.example.artvswar.dto.response.EntitiesPageResponse;
import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.util.EntitiesResponseCreator;
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
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final EntitiesResponseCreator creator;

    @GetMapping
    public ResponseEntity<EntitiesPageResponse<AuthorResponseDto>> getAllAuthors(
            @RequestParam Map<String, String> params) {
        Page<Author> pages = authorService.getAll(params);
        List<AuthorResponseDto> allListOfAuthors = pages.stream()
                .map(authorMapper::toAuthorResponseDto)
                .collect(Collectors.toList());
        return creator.createResponse(allListOfAuthors, pages);
    }

    @GetMapping("/{id}")
    public AuthorResponseDto getById(@PathVariable String id) {
        Author author = authorService.get(id);
        return authorMapper.toAuthorResponseDto(author);
    }

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDto create(@RequestBody @Valid AuthorRequestDto dto,
                       @AuthenticationPrincipal Jwt jwt) {
        Author author = authorMapper.toAuthorModel(dto);
        author.setId(jwt.getClaimAsString("username"));
        return authorMapper.toAuthorResponseDto(authorService.save(author));
    }

    @PutMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.OK)
    public AuthorResponseDto update(@RequestBody @Valid AuthorRequestDto dto,
                                    @AuthenticationPrincipal Jwt jwt) {
        Author author = authorMapper.toAuthorModel(dto);
        author.setId(jwt.getClaimAsString("username"));
        return authorMapper.toAuthorResponseDto(authorService.update(author));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getNumberOfAllAuthors() {
        return new ResponseEntity<>(authorService.getNumberOfAllAuthors(),
                HttpStatus.OK);
    }
}
