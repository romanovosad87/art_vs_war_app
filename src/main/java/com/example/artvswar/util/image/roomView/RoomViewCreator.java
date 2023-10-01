package com.example.artvswar.util.image.roomView;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.transformation.Layer;
import com.cloudinary.utils.ObjectUtils;
import com.example.artvswar.model.RoomView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RoomViewCreator {
    private static final String TYPE_KEY = "type";
    private static final String TYPE_OF_UPLOAD = "upload";
    private static final String COLOR = "#525252";
    private static final String CROP_TYPE = "scale";
    private static final String EFFECT = "shadow:40";
    private static final String EAGER = "eager";
    private static final int SHADOW_LENGTH = 3;
    private static final String QUALITY_TYPE = "auto";
    private static final String FETCH_FORMAT_TYPE = "auto";
    private static final String GRAVITY_DIRECTION = "north";
    private static final String SECURE_URL = "secure_url";
    private static final int ZERO = 0;
    private final Cloudinary cloudinary;

    public RoomView createRoomView(String publicId,
                                   long widthOfPaintingInPixels,
                                   long yValue,
                                   long xValue,
                                   String underlayPublicId) {
        try {
            Map<String, Object> eagerUpload = cloudinary.uploader().explicit(publicId, ObjectUtils.asMap(
                    TYPE_KEY, TYPE_OF_UPLOAD,
                    EAGER, Collections.singletonList(new Transformation<>()
                            .color(COLOR).width(widthOfPaintingInPixels).crop(CROP_TYPE)
                            .effect(EFFECT).x(SHADOW_LENGTH).y(SHADOW_LENGTH).chain()
                            .quality(QUALITY_TYPE).fetchFormat(FETCH_FORMAT_TYPE).chain()
                            .underlay(new Layer().publicId(underlayPublicId))
                            .gravity(GRAVITY_DIRECTION).y(yValue).x(xValue))));
            List<Map<String, Object>> eager = (List<Map<String, Object>>)eagerUpload.get(EAGER);
            RoomView roomView = new RoomView();
            roomView.setImageUrl((String) eager.get(ZERO).get(SECURE_URL));
            return roomView;
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Can't eager upload room view transformation for asset with public id = %s",
                            publicId), e);
        }
    }
}
