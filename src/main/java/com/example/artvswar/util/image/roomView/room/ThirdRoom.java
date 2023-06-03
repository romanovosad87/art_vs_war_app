package com.example.artvswar.util.image.roomView.room;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.transformation.Layer;
import com.cloudinary.utils.ObjectUtils;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.util.image.roomView.RoomViewCreator;
import com.example.artvswar.util.image.roomView.RoomViewProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ThirdRoom implements RoomViewProvider {
    private static final double RATIO = 1.0;
    private static final String underlayPublicId = "art-app/rooms/room-commode-lamp-plant-w1418";
    private static final int underlayPixelWidth = 1418;
    private static final int underlayInCentimetersWidth = 260;
    private final Cloudinary cloudinary;
    private final RoomViewCreator roomViewCreator;

    @Override
    public RoomView getRoom(String publicId, double paintingWidth,
                            double paintingHeight) {
        long widthPixels = Math.round(paintingWidth * underlayPixelWidth / underlayInCentimetersWidth);
        long yValue = Math.round(-200 + ((paintingHeight - 50) / 70) * 100);
        RoomView roomView = null;
        if (paintingWidth <= 120 && paintingWidth > 50 && paintingHeight <= 120 && paintingHeight > 50) {
            try {
                Map<String, Object> eagerUpload = cloudinary.uploader().explicit(publicId, ObjectUtils.asMap(
                        "type", "upload",
                        "eager", Arrays.asList(new Transformation()
                                .color("#525252").width(widthPixels).crop("scale")
                                .effect("shadow:50").x(4).y(4).chain()
                                .quality("auto").fetchFormat("auto").chain()
                                .underlay(new Layer().publicId(underlayPublicId))
                                .gravity("north").y(yValue))));
                roomView = roomViewCreator.createRoomView(eagerUpload, RATIO);
            } catch (IOException e) {
                throw new RuntimeException(
                        String.format("Can't eager upload room view transformation for asset with public id = %s",
                                publicId), e);
            }

        }
        return roomView;
    }
}
