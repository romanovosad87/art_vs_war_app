package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.AuthorMapper;
import com.example.artvswar.dto.mapper.PaintingMapper;
import com.example.artvswar.dto.request.AuthorRequestDto;
import com.example.artvswar.dto.response.EntitiesPageResponse;
import com.example.artvswar.dto.response.RefreshTokenResponse;
import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.util.AwsCognitoClient;
import com.example.artvswar.util.EntitiesResponseCreator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import com.example.artvswar.util.RefreshTokenCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final PaintingMapper paintingMapper;
    private final EntitiesResponseCreator entitiesCreator;
    private final AwsCognitoClient awsCognitoClient;
    private final RefreshTokenCreator tokenCreator;

    @GetMapping
    public ResponseEntity<EntitiesPageResponse<AuthorResponseDto>> getAllAuthors(
            @RequestParam Map<String, String> params) {
        Page<Author> pages = authorService.getAll(params);
        List<AuthorResponseDto> allListOfAuthors = pages.stream()
                .map(authorMapper::toAuthorResponseDto)
                .collect(Collectors.toList());
        return entitiesCreator.createResponse(allListOfAuthors, pages);
    }

    @GetMapping("/{id}")
    public AuthorResponseDto getById(@PathVariable String id) {
        Author author = authorService.get(id);
        return authorMapper.toAuthorResponseDto(author);
    }

    @PostMapping
//    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDto create(@RequestBody @Valid AuthorRequestDto dto,
                                    @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        Author author = authorMapper.toAuthorModel(dto);
        author.setId(username);
        Author savedAuthor = authorService.save(author);
        awsCognitoClient.addUserToGroup(username, "ROLE_AUTHOR");
        return authorMapper.toAuthorResponseDto(savedAuthor);
    }

    @PostMapping(value = "/initAuth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<RefreshTokenResponse> initAuthentication(String refreshToken) {
       return tokenCreator.createResponse(awsCognitoClient.getNewTokens(refreshToken));
    }

    @PutMapping
//    @PreAuthorize("hasRole('AUTHOR')")
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

//    @GetMapping("/paintings/{id}")
//    public List<PaintingResponseDto> getAllPaintingsByAuthorId(@PathVariable String id) {
//        Author author = authorService.get(id);
//        return author.getPaintings().stream()
//                .map(paintingMapper::toPaintingResponseDto)
//                .collect(Collectors.toList());
//    }
}
