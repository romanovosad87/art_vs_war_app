package com.example.artvswar.util.image.roomView.room;

import com.example.artvswar.model.RoomView;
import com.example.artvswar.service.MockRoomService;
import com.example.artvswar.util.image.roomView.RoomViewCreator;
import com.example.artvswar.util.image.roomView.RoomViewProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EighteenthRoom implements RoomViewProvider {
    private static final int UNDERLAY_PIXEL_WIDTH = 1418;
    private static final int UNDERLAY_IN_CENTIMETERS_WIDTH = 120;
    private static final long UNDERLAY_ROOM_ID = 18L;
    private final RoomViewCreator roomViewCreator;
    private final MockRoomService mockRoomService;

    @Override
    public RoomView getRoom(String publicId, double paintingWidth, double paintingHeight) {
        long widthOfPaintingInPixels = Math.round(
                paintingWidth * UNDERLAY_PIXEL_WIDTH / UNDERLAY_IN_CENTIMETERS_WIDTH);
        long yValue = Math.round(-180 + ((paintingHeight - 19) / 11) * 20);
        long xValue = -60;
        if (paintingWidth <= 30 && paintingWidth > 19 && paintingHeight <= 30 && paintingHeight > 19) {
            String underlayPublicId = mockRoomService.getPublicId(UNDERLAY_ROOM_ID);
            return roomViewCreator.createRoomView(publicId,
                    widthOfPaintingInPixels,
                    yValue, xValue,
                    underlayPublicId);
        }
        return null;
    }
}
