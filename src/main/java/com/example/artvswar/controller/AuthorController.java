package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.AuthorMapper;
import com.example.artvswar.dto.request.author.AuthorCreateRequestDto;
import com.example.artvswar.dto.response.author.AuthorCreatedDto;
import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.model.Author;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.util.RefreshTokenCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final RefreshTokenCreator tokenCreator;

    @GetMapping
    public ResponseEntity<Page<AuthorResponseDto>> getAllAuthors(@PageableDefault(size = 12) Pageable pageable) {
        Page<AuthorResponseDto> allAuthors = authorService.getAll(pageable);
        return new ResponseEntity<>(allAuthors, HttpStatus.OK);
    }

    @GetMapping("/{cognitoUsername}")
    public ResponseEntity<AuthorResponseDto> getByCognitoUsername(@PathVariable String cognitoUsername) {
        AuthorResponseDto authorResponseDto = authorService.getDtoByCognitoUsername(cognitoUsername);
        return new ResponseEntity<>(authorResponseDto, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorCreatedDto> create(@RequestBody @Valid AuthorCreateRequestDto dto,
                                    @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("username");
        Author author = authorMapper.toAuthorModel(dto);
        author.setCognitoUsername(username);
        Author savedUser = authorService.save(author);
        return new ResponseEntity<>(new AuthorCreatedDto(savedUser.getCognitoUsername()),
                HttpStatus.CREATED);
    }

//    @PostMapping(value = "/initAuth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<RefreshTokenResponse> initAuthentication(String refreshToken) {
//       return tokenCreator.createResponse(awsCognitoClient.getNewTokens(refreshToken));
//    }

//    @PutMapping
//    @PreAuthorize("hasRole('AUTHOR')")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void update(@RequestBody @Valid AuthorUpdateRequestDto dto,
//                                    @AuthenticationPrincipal Jwt jwt) {
//        String username = jwt.getClaimAsString("username");
//        Author author = authorMapper.toAuthorModel(dto);
//        author.setCognitoUsername(username);
//        authorService.update(author);
//    }

    @GetMapping("/count")
    public ResponseEntity<Long> getNumberOfAllAuthors() {
        return new ResponseEntity<>(authorService.getNumberOfAllAuthors(),
                HttpStatus.OK);
    }
}
