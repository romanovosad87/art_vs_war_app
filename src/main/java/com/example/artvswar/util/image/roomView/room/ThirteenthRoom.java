package com.example.artvswar.util.image.roomView.room;

import com.example.artvswar.model.RoomView;
import com.example.artvswar.service.MockRoomService;
import com.example.artvswar.util.image.roomView.RoomViewCreator;
import com.example.artvswar.util.image.roomView.RoomViewProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ThirteenthRoom implements RoomViewProvider {
    private static final int UNDERLAY_PIXEL_WIDTH = 1418;
    private static final int UNDERLAY_IN_CENTIMETERS_WIDTH = 340;
    private static final long UNDERLAY_ROOM_ID = 13L;
    private final RoomViewCreator roomViewCreator;
    private final MockRoomService mockRoomService;
    @Override
    public RoomView getRoom(String publicId, double paintingWidth, double paintingHeight) {
        long widthOfPaintingPixels = Math.round(
                paintingWidth * UNDERLAY_PIXEL_WIDTH / UNDERLAY_IN_CENTIMETERS_WIDTH);
        long yValue = Math.round(-100 + ((paintingHeight - 39) / 31) * 50);
        long xValue = 20;
        if (paintingWidth <= 70 && paintingWidth > 39 && paintingHeight <= 70 && paintingHeight > 39) {
            String underlayPublicId = mockRoomService.getPublicId(UNDERLAY_ROOM_ID);
            return roomViewCreator.createRoomView(publicId,
                    widthOfPaintingPixels,
                    yValue, xValue,
                    underlayPublicId);
        }
        return null;
    }
}
