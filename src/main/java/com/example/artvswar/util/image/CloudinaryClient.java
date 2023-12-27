package com.example.artvswar.util.image;

import static java.time.LocalDateTime.now;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.api.signing.ApiResponseSignatureVerifier;
import com.cloudinary.api.signing.NotificationRequestSignatureVerifier;
import com.cloudinary.utils.ObjectUtils;
import com.example.artvswar.dto.response.image.SignatureResponse;
import com.example.artvswar.exception.CloudinaryClientException;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryClient {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS", Locale.UK);
    public static final String REJECTED = "rejected";
    public static final String APPROVED = "approved";
    public static final String MODERATION_STATUS = "moderation_status";
    private final Cloudinary cloudinary;
    @Value("${cloudinary.secret}")
    private String cloudinarySecret;

    public SignatureResponse getSignature(Map<String, Object> params) {
        long timestamp = Instant.now().getEpochSecond();
        params.put("timestamp", timestamp);
        String signature = cloudinary.apiSignRequest(
                params,
                cloudinarySecret);

        SignatureResponse response = new SignatureResponse();
        response.setSignature(signature);
        response.setTimestamp(timestamp);
        return response;
    }

    public boolean verifySignature(String publicId, String version, String signature) {
        ApiResponseSignatureVerifier apiResponseSignatureVerifier = new ApiResponseSignatureVerifier(cloudinarySecret);
        return apiResponseSignatureVerifier.verifySignature(publicId, version, signature);
    }

    public boolean verifyNotification(String body, String timestamp, String signature) {
        NotificationRequestSignatureVerifier signatureVerifier = new NotificationRequestSignatureVerifier(cloudinarySecret);
        return signatureVerifier.verifySignature(body, timestamp, signature);
    }

    public void changeModerationStatusToApproved(String publicId, String adminUsername) {
        try {
            cloudinary.api().update(publicId,
                    ObjectUtils.asMap(
                            MODERATION_STATUS, APPROVED));
            addTag(publicId, adminUsername,APPROVED);
        } catch (Exception e) {
            throw new CloudinaryClientException(
                    String.format("Can't change moderation status to "
                            + "approved of the image with id = %s", publicId), e);
        }
    }

    public void changeModerationStatusToRejected(String publicId, String adminUsername){
        try {
            cloudinary.api().update(publicId,
                    ObjectUtils.asMap(
                            MODERATION_STATUS, REJECTED));
            addTag(publicId, adminUsername, REJECTED);
        } catch (Exception e) {
            throw new CloudinaryClientException(
                    String.format("Can't change moderation status to "
                            + "rejected of the image with id = %s", publicId), e);
        }
    }

    public void addTag(String publicId, String adminUsername, String operation) {
        try {
            cloudinary.uploader().addTag(String.format("moderator that %s: %s time: %s",
                            operation, adminUsername, now().format(DATE_TIME_FORMATTER)),
                    new String[] {publicId}, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new CloudinaryClientException(
                    String.format("Can't add tag to image with public_id: '%s'", publicId), e);
        }
    }

    public ApiResponse listRejectedAssets() {
        try {
            return cloudinary.api()
                    .resourcesByModeration("aws_rek", REJECTED,
                            ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new CloudinaryClientException("Can't list all assets rejected by aws-rekognition", e);
        }
    }

    public ApiResponse getDetailsByPublicId(String publicId) {
        try {
             return cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new CloudinaryClientException(
                    String.format("Can't find resource by public_id = %s", publicId), e);
        }
    }

    public String delete(String publicId) {
        try {
            Map<String, String> destroy = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
            return destroy.get("result");
        } catch (IOException e) {
            throw new CloudinaryClientException(
                    String.format("Can't delete asset from Cloudinary with id: %s", publicId), e);
        }
    }

    public void addTagOnManualModeration(String body) throws JSONException {
        JSONObject object = new JSONObject(body);
        String moderationStatus = object.getString(MODERATION_STATUS);
        String publicId = object.getString("public_id");
        String moderatedAt = object.getString("moderation_updated_at");
        String source = object.getJSONObject("notification_context").getJSONObject("triggered_by").getString("source");
        String id = object.getJSONObject("notification_context").getJSONObject("triggered_by").getString("id");
        try {
            cloudinary.uploader().addTag("moderation: status " + moderationStatus + System.lineSeparator()
                            + " source  - " + source + System.lineSeparator()
                            + " id: " + id + System.lineSeparator()
                            + " time: " + moderatedAt,
                    new String[] { publicId }, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new CloudinaryClientException(
                    String.format("Can't add tag to "
                            + " the image with id = %s", publicId), e);
        }
    }
}
