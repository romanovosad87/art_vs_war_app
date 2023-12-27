package com.example.artvswar.controller;

import com.cloudinary.api.ApiResponse;
import com.example.artvswar.dto.request.image.ImageModerationStatusRequestDto;
import com.example.artvswar.dto.response.image.PendingRejectImageResponse;
import com.example.artvswar.dto.response.image.SignatureResponse;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.service.PaintingImageService;
import com.example.artvswar.service.impl.CloudinaryImageService;
import com.example.artvswar.util.image.CloudinaryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private static final String USERNAME = "username";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private final CloudinaryImageService cloudinaryImageService;
    private final PaintingImageService paintingImageService;
    private final CloudinaryClient cloudinaryClient;
    private final ImageService imageService;

    @PostMapping("/getSignature")
    public ResponseEntity<SignatureResponse> getSignature(@RequestBody Map<String, Object> params) {
        return new ResponseEntity<>(cloudinaryImageService.getSignature(params), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/setApproved")
    public ResponseEntity<String> changeModerationStatusToApproved(@RequestParam String publicId,
                                                                   @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString(USERNAME);
        cloudinaryImageService.changeModerationStatusToApproved(publicId, username);
        return new ResponseEntity<>("moderation status changed to approved", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/changeToRejected")
    public ResponseEntity<String> changeModerationStatusToRejected(@RequestParam String publicId,
                                                                   @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString(USERNAME);
        cloudinaryImageService.changeModerationStatusToRejected(publicId, username);
        return new ResponseEntity<>("moderation status changed to rejected", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rejected")
    public ResponseEntity<Page<PendingRejectImageResponse>> listRejectedImages(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<PendingRejectImageResponse> rejectedImages = imageService.getRejectedImages(pageable);
        return new ResponseEntity<>(rejectedImages, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<Page<PendingRejectImageResponse>> listPendingImages(
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<PendingRejectImageResponse> pendingImages = imageService.getPendingImages(pageable);
        return new ResponseEntity<>(pendingImages, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/setStatus")
    public ResponseEntity<String> changeStatusToRejectedInDB(@RequestBody ImageModerationStatusRequestDto dto,
                                                             @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString(USERNAME);
        imageService.changeModerationStatus(dto.getPublicId(), username, dto.getStatus());
        return new ResponseEntity<>(String.format("moderation status changed "
                + "to %s for image public_id: '%s'", dto.getStatus(), dto.getPublicId()), HttpStatus.OK);
    }

    @PostMapping("/moderation")
    @ResponseStatus(HttpStatus.OK)
    public String moderationWebhook(@RequestHeader("X-Cld-Timestamp") String timestamp,
                                    @RequestHeader("X-Cld-Signature") String signature,
                                    @RequestBody String body) throws JSONException {
        boolean verifyNotification = cloudinaryClient.verifyNotification(body, timestamp, signature);
        if (verifyNotification) {
            cloudinaryImageService.handleModerationResponse(body);
        }
        return "ok";
    }

    @GetMapping("/asset")
    public ApiResponse getAsset(@RequestParam String publicId) {
        return cloudinaryImageService.getAssetDetails(publicId);
    }

    @DeleteMapping("/paintingImage")
    public void delete(@RequestParam Long id) {
        paintingImageService.delete(id);
    }
}
