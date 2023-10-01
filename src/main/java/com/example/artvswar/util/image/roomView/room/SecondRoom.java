package com.example.artvswar.util.image.roomView.room;

import com.example.artvswar.model.RoomView;
import com.example.artvswar.service.MockRoomService;
import com.example.artvswar.util.image.roomView.RoomViewCreator;
import com.example.artvswar.util.image.roomView.RoomViewProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecondRoom implements RoomViewProvider {
    private static final int UNDERLAY_PIXEL_WIDTH = 945;
    private static final int UNDERLAY_IN_CENTIMETERS_WIDTH = 133;
    private static final long UNDERLAY_ROOM_ID = 2L;
    private final RoomViewCreator roomViewCreator;
    private final MockRoomService mockRoomService;

    @Override
    public RoomView getRoom(String publicId, double paintingWidth,
                            double paintingHeight) {
        long widthOfPaintingInPixels = Math.round(
                paintingWidth * UNDERLAY_PIXEL_WIDTH / UNDERLAY_IN_CENTIMETERS_WIDTH);
        long yValue = Math.round(-200 + ((paintingHeight - 24) / 26) * 100);
        long xValue = 0;
        if (paintingWidth <= 50 && paintingWidth > 24 && paintingHeight <= 50 && paintingHeight > 24) {
            String underlayPublicId = mockRoomService.getPublicId(UNDERLAY_ROOM_ID);
            return roomViewCreator.createRoomView(publicId,
                    widthOfPaintingInPixels,
                    yValue, xValue,
                    underlayPublicId);
        }
        return null;
    }
}
