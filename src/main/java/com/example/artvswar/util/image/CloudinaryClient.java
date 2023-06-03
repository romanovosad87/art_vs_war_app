package com.example.artvswar.util.image;

import static java.time.LocalDateTime.now;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.signing.ApiResponseSignatureVerifier;
import com.cloudinary.utils.ObjectUtils;
import com.example.artvswar.dto.response.image.SignatureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CloudinaryClient {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS", Locale.UK);
    private final Cloudinary cloudinary;
    @Value("${cloudinary.secret}")
    private String cloudinarySecret;

    public SignatureResponse getSignature() {
        long timestamp = Instant.now().getEpochSecond();
        String signature = cloudinary.apiSignRequest(
                ObjectUtils.asMap("timestamp", timestamp, "source", "uw", "upload_preset", "signed-image"), cloudinarySecret);

        SignatureResponse response = new SignatureResponse();
        response.setSignature(signature);
        response.setTimestamp(timestamp);
        return response;
    }

    public boolean verifySignature(String publicId, String version, String signature) {
        ApiResponseSignatureVerifier apiResponseSignatureVerifier = new ApiResponseSignatureVerifier(cloudinarySecret);
        return apiResponseSignatureVerifier.verifySignature(publicId, version, signature);
    }

    public void changeModerationStatusToApproved(String publicId) {
        try {
            cloudinary.api().update(publicId,
                    ObjectUtils.asMap(
                            "moderation_status", "approved"));
            cloudinary.uploader().addTag("moderator that approved" + " id =  " + now().format(DATE_TIME_FORMATTER), new String[] { publicId }, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Can't change moderation status to "
                            + "approved of the image with id = %s", publicId), e);
        }
    }

    public void changeModerationStatusToRejected(String publicId){
        try {
            cloudinary.api().update(publicId,
                    ObjectUtils.asMap(
                            "moderation_status", "rejected"));
            cloudinary.uploader().addTag("moderator that rejected " + " id =  " + now().format(DATE_TIME_FORMATTER), new String[] { publicId }, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Can't change moderation status to "
                            + "rejected of the image with id = %s", publicId), e);
        }
    }

//    public void makePainting(int height, int width, int opacity, int border, String secureUrl) {
//        Transformation transformation1 = transformation.transformTo3d(height, width, 0.6, 20);
//        String toString = transformation1.toString();
//        try {
//            cloudinary.uploader()
//                             .upload(secureUrl,
//                 ObjectUtils.asMap("upload_preset", "signed-image-with-moderation",
//                         "transformation", toString));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
