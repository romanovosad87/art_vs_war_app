package com.example.artvswar.service.impl;

import com.cloudinary.api.ApiResponse;
import com.example.artvswar.dto.response.image.SignatureResponse;
import com.example.artvswar.dto.response.painingImage.PaintingImageResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.repository.paintingImage.PaintingImageRepository;
import com.example.artvswar.service.EmailService;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.roomView.RoomViewManager;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CloudinaryImageService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS", Locale.UK);
    private final CloudinaryClient cloudinaryClient;
    private final ImageService imageService;
    private final PaintingImageRepository paintingImageRepository;
    private final RoomViewManager roomViewManager;
    private final EmailService emailService;

    public SignatureResponse getSignature(Map<String, Object> params) {
        return cloudinaryClient.getSignature(params);
    }


    @Transactional
    public PaintingImage save(PaintingImage paintingImage, double paintingWidth,
                              double paintingHeight) {
        List<RoomView> viewRoomsList = roomViewManager.getViewRooms(paintingImage.getImage().getPublicId(),
                paintingWidth, paintingHeight);
        paintingImage.getRoomViews().addAll(viewRoomsList);
        return paintingImageRepository.save(paintingImage);
    }

    public PaintingImage get(Long id) {
        return paintingImageRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(String.format("Can't find image by id = %s", id)));
    }

    public void changeModerationStatusToApproved(String publicId, String adminUsername) {
        cloudinaryClient.changeModerationStatusToApproved(publicId, adminUsername);
    }

    public void changeModerationStatusToRejected(String publicId, String adminUsername) {
        cloudinaryClient.changeModerationStatusToRejected(publicId, adminUsername);
    }

    public ApiResponse listRejectedAssets() {
        return cloudinaryClient.listRejectedAssets();
    }

    public void handleModerationResponse(String body) throws JSONException {
        JSONObject object = new JSONObject(body);
        String moderationKind = object.getString("moderation_kind");
        String moderationStatus = object.getString("moderation_status");
        String publicId = object.getString("public_id");
        String moderationResponse = object.getString("moderation_response");
        if (moderationKind.equals("aws_rek") && moderationStatus.equals("rejected")) {
            emailService.sendImageRejectionMail(publicId, moderationResponse);
        }

        if (moderationKind.equals("manual")) {
            imageService.updateModerationStatus(publicId, moderationStatus);
            cloudinaryClient.addTagOnManualModeration(body);
        }
    }

    private List<PaintingImageResponseDto> parseObject(List<Object[]> response) {
        return response.stream()
                .map(obj -> new PaintingImageResponseDto((BigInteger)obj[0], (Double) obj[1], (String) obj[2]))
                .collect(Collectors.toList());
    }
}