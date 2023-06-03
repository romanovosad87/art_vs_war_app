package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.image.SignatureResponse;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.repository.ImageRepository;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.ImageTransformation;
import com.example.artvswar.util.image.roomView.RoomViewManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CloudinaryImageServiceImpl {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS", Locale.UK);
    private final CloudinaryClient cloudinaryClient;
    private final ImageTransformation imageTransformation;
    private final ImageRepository imageRepository;
    private final RoomViewManager roomViewManager;

    public SignatureResponse getSignature() {
        return cloudinaryClient.getSignature();
    }


    @Transactional
    public Image save(Image image, double paintingWidth, double paintingHeight) {
        List<RoomView> viewRoomsList = roomViewManager.getViewRooms(image.getId(),
                paintingWidth, paintingHeight);
        image.getRoomViews().addAll(viewRoomsList);
        return imageRepository.save(image);
    }

    public Image get(String id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find image by id = %s", id)));
    }

//    @Transactional(readOnly = true)
//    public List<Image> getAll() {
//        return imageRepository.findAll();
//    }
//
//    public ImageResponse getImageUrl(String publicId) {
//        Image image = imageRepository.findById(publicId).orElseThrow(
//                () -> new AppEntityNotFoundException(
//                        String.format("Can't find image by id = %s", publicId)));
//        return imageTransformation.getImageResponse(image);
//    }

    public List<Image> getImagesForMainPage() {
        return imageRepository.findByTransformedRatioAndLimit(0.5, 1, 1, 1, 1.5, 1);
//        List<Image> ratioZeroPointFive = imageRepository.findByTransformedRatio(0.5, 2);
//        List<Image> ratioOne = imageRepository.findByTransformedRatio(1, 2);
//        List<Image> ratioOnePointFive = imageRepository.findByTransformedRatio(1.5, 1);
//        List<Image> joinedList = new ArrayList<>();
//        Stream.of(ratioZeroPointFive, ratioOne, ratioOnePointFive).forEach(joinedList::addAll);
//        return joinedList;
    }

    public void changeModerationStatusToApproved(String publicId) {
        cloudinaryClient.changeModerationStatusToApproved(publicId);
    }

    public void changeModerationStatusToRejected(String publicId) {
        cloudinaryClient.changeModerationStatusToRejected(publicId);
    }
}