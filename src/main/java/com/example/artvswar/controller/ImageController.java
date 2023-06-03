package com.example.artvswar.controller;

import com.example.artvswar.dto.mapper.ImageMapper;
import com.example.artvswar.dto.response.image.ImageResponse;
import com.example.artvswar.dto.response.image.SignatureResponse;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.service.impl.CloudinaryImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final CloudinaryImageServiceImpl cloudinaryImageService;
    private final ImageMapper imageMapper;

//    @GetMapping("/getUrl")
//    public ResponseEntity<Object> getUrlToSaveImage(@RequestParam("extension")
//                                                    @Size(min = 3, max = 4) String extension) {
//        return new ResponseEntity<>(imageService.generatePutUrl(extension), HttpStatus.OK);
//    }
//
////    @GetMapping("/{id}")
////    public ResponseEntity<Object> getImage(@PathVariable Long id) {
////        Painting painting = paintingService.get(id);
////        return new ResponseEntity<>(imageService.generateGetUrl(painting.getImageFileName()),
////                HttpStatus.OK);
////    }

    @GetMapping("/getSignature")
    public ResponseEntity<SignatureResponse> getSignature() {
        return new ResponseEntity<>(cloudinaryImageService.getSignature(), HttpStatus.OK);
    }

    @GetMapping("/mainPage")
    public List<ImageResponse> getForMainPage() {
        return cloudinaryImageService.getImagesForMainPage().stream()
                .map(imageMapper::toImageResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/getRoomViews/{publicId}")
    public ResponseEntity<Set<RoomView>> getAllRoomViews(@PathVariable String publicId) {
        Image image = cloudinaryImageService.get(publicId);
        Set<RoomView> roomViews = image.getRoomViews();
        return new ResponseEntity<>(roomViews, HttpStatus.OK);
    }

    @GetMapping("/changeToApproved/{publicId}")
    public ResponseEntity<String> changeModerationStatusToApproved(@PathVariable String publicId) {
        cloudinaryImageService.changeModerationStatusToApproved(publicId);
        return new ResponseEntity<>("moderation status changed to approved", HttpStatus.OK);
    }

    @GetMapping("/changeToRejected/{publicId}")
    public ResponseEntity<String> changeModerationStatusToREjected(@PathVariable String publicId) {
        cloudinaryImageService.changeModerationStatusToRejected(publicId);
        return new ResponseEntity<>("moderation status changed to rejected", HttpStatus.OK);
    }

}
