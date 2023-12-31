package com.example.artvswar.service.impl;

import com.cloudinary.api.ApiResponse;
import com.example.artvswar.dto.response.image.PendingRejectImageResponse;
import com.example.artvswar.dto.response.image.SignatureResponse;
import com.example.artvswar.dto.response.painingimage.PaintingImageResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.repository.paintingimage.PaintingImageRepository;
import com.example.artvswar.service.EmailService;
import com.example.artvswar.service.ImageService;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.roomView.RoomViewManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CloudinaryImageService {

    private static final String CLOUDINARY_URL = "https://console.cloudinary.com/console/"
            + "c-1e98a800837a96a82947e8ca3b3fd0/media_library/moderation/asset/%s/manage/summary"
            + "?view_mode=grid&q=&status=rejected&type=aws_rek&context=manage";
    private static final DateTimeFormatter DATE_TIME_FORMATTER_PRETTY =
            DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", Locale.UK);

    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static final String RESOURCES = "resources";
    public static final String PUBLIC_ID = "public_id";
    public static final String CREATED_AT = "created_at";
    public static final String ASSET_ID = "asset_id";
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

    @SneakyThrows
    public List<PendingRejectImageResponse> listRejectedAssets() {
        List<PendingRejectImageResponse> rejectAssetsResponses = new ArrayList<>();
        ApiResponse rejectedAssets = cloudinaryClient.listRejectedAssets();
        JSONObject jsonObject = new JSONObject(rejectedAssets);
        JSONArray resources = jsonObject.getJSONArray(RESOURCES);
        for (int i = 0; i < resources.length(); i++) {
            JSONObject object = resources.getJSONObject(i);
            String publicId = object.getString(PUBLIC_ID);
            String createdAt = object.getString(CREATED_AT);
            LocalDateTime localDateTime = LocalDateTime.parse(createdAt, DATE_TIME_FORMATTER);
            String assetId = object.getString(ASSET_ID);
            String url = String.format(CLOUDINARY_URL, assetId);
            imageService.getImage(publicId)
                    .ifPresentOrElse(image -> rejectAssetsResponses.add(
                            new PendingRejectImageResponse(publicId, localDateTime, url, image.getModerationStatus())),
                            () -> cloudinaryClient.delete(publicId));
        }
        return rejectAssetsResponses;
    }

    @Transactional
    public void handleModerationResponse(String body) throws JSONException {
        JSONObject object = new JSONObject(body);
        String moderationKind = object.getString("moderation_kind");
        String moderationStatus = object.getString("moderation_status");
        String publicId = object.getString(PUBLIC_ID);
        if (moderationKind.equals("aws_rek") && moderationStatus.equals("rejected")) {
            String moderationResponse = object.getString("moderation_response");
            emailService.sendImageRejectionMail(publicId, moderationResponse);
        }

        if (moderationKind.equals("manual")) {
            imageService.updateModerationStatus(publicId, moderationStatus);
            cloudinaryClient.addTagOnManualModeration(body);
        }
    }

    public ApiResponse getAssetDetails(String publicId) {
        return cloudinaryClient.getDetailsByPublicId(publicId);
    }

    private List<PaintingImageResponseDto> parseObject(List<Object[]> response) {
        return response.stream()
                .map(obj -> new PaintingImageResponseDto((BigInteger)obj[0], (Double) obj[1], (String) obj[2]))
                .collect(Collectors.toList());
    }
}