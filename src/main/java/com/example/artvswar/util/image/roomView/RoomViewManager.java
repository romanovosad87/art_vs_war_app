package com.example.artvswar.util.image.roomView;

import com.example.artvswar.model.RoomView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomViewManager {
    private final List<RoomViewProvider> roomViewProviderList;

    public List<RoomView> getViewRooms(String publicId, double paintingWidth,
                                       double paintingHeight) {
        return roomViewProviderList.stream()
                .parallel()
                .map(provider -> provider.getRoom(publicId, paintingWidth, paintingHeight))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
