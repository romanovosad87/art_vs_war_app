package com.example.artvswar.controller;

import com.example.artvswar.dto.request.artProcess.ArtProcessCreateRequestDto;
import com.example.artvswar.dto.request.artProcess.ArtProcessUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.artProcess.ArtProcessResponseDto;
import com.example.artvswar.service.ArtProcessService;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/artProcess")
@RequiredArgsConstructor
public class ArtProcessController {
    private static final String SUBJECT = "sub";
    private final ArtProcessService artProcessService;

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ArtProcessResponseDto> create(
            @Valid @RequestBody ArtProcessCreateRequestDto dto,
            @AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        ArtProcessResponseDto responseDto = artProcessService.save(dto, cognitoSubject);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<ArtProcessResponseDto> update(@PathVariable Long id,
                                                    @Valid @RequestBody ArtProcessUpdateRequestDto dto,
                                                    @AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        ArtProcessResponseDto responseDto = artProcessService.update(id, dto, cognitoSubject);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('AUTHOR')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@RequestParam Long id) {
        artProcessService.delete(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<List<ArtProcessResponseDto>> getAllByAuthorCognitoSubject(
            @AuthenticationPrincipal Jwt jwt) {
        String cognitoSubject = jwt.getClaimAsString(SUBJECT);
        List<ArtProcessResponseDto> allByAuthorCognitoSubject = artProcessService
                .getAllByAuthorCognitoSubject(cognitoSubject);
        return new ResponseEntity<>(allByAuthorCognitoSubject, HttpStatus.OK);
    }

    @GetMapping("/all/{authorPrettyId}")
    public ResponseEntity<List<ArtProcessResponseDto>> getAllByAuthorPrettyId(
            @PathVariable String authorPrettyId) {
        List<ArtProcessResponseDto> allByAuthorId = artProcessService
                .getAllByAuthorPrettyId(authorPrettyId);
        return new ResponseEntity<>(allByAuthorId, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ArtProcessResponseDto> getById(@RequestParam Long id) {
        ArtProcessResponseDto dto = artProcessService.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/getFolder")
    public FolderResponseDto checkInputParametersAndReturnCloudinaryFolder(
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getClaimAsString(SUBJECT);
        return artProcessService.createCloudinaryFolder(subject);
    }
}
