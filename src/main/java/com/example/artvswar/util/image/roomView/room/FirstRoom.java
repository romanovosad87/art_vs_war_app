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
public class FirstRoom implements RoomViewProvider {
    private static final double RATIO = 1.5;
    private static final String underlayPublicId = "art-app/rooms/small-cacti_w1418";
    private static final int underlayPixelWidth = 1418;
    private static final int underlayInCentimetersWidth = 90;
    private final Cloudinary cloudinary;
    private final RoomViewCreator roomViewCreator;

    @Override
    public RoomView getRoom(String publicId, double paintingWidth,
                            double paintingHeight) {
        long widthOfPaintingPixels = Math.round(paintingWidth * underlayPixelWidth / underlayInCentimetersWidth);
        long yValue = Math.round(-300 + ((paintingHeight) / 25) * 150);
        RoomView roomView = null;
        if (paintingWidth < 25 && paintingHeight < 25) {
            try {
                Map<String, Object> eagerUpload = cloudinary.uploader().explicit(publicId, ObjectUtils.asMap(
                        "type", "upload",
                        "eager", Arrays.asList(new Transformation()
                                .color("#525252").width(widthOfPaintingPixels).crop("scale")
                                .effect("shadow:30").x(2).y(2).chain()
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
