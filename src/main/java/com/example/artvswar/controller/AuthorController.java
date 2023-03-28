package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.AuthorMapper;
import com.example.artvswar.dto.request.AuthorRequestDto;
import com.example.artvswar.dto.response.AuthorResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.service.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @GetMapping("/all")
    public List<AuthorResponseDto> getAllAuthors() {
        return authorService.getAllAuthors().stream()
                .map(authorMapper::toAuthorResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AuthorResponseDto getById(@PathVariable Long id) {
        Author author = authorService.get(id);
        return authorMapper.toAuthorResponseDto(author);
    }

    @PostMapping
    private AuthorResponseDto create(@RequestBody AuthorRequestDto dto) {
        Author author = authorMapper.toAuthorModel(dto);
        return authorMapper.toAuthorResponseDto(authorService.save(author));
    }
}
