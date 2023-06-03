package com.example.artvswar.util.image.roomView;

import com.example.artvswar.model.RoomView;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class RoomViewCreator {
    private static final String EAGER = "eager";
    private static final String SECURE_URL = "secure_url";
    private static final int ZERO = 0;

    public RoomView createRoomView(Map<String, Object> eagerTransformation, double ratio) {
        List<Map<String, Object>> eager = (List<Map<String, Object>>) eagerTransformation.get(EAGER);
        RoomView roomView = new RoomView();
        roomView.setImageUrl((String) eager.get(ZERO).get(SECURE_URL));
        roomView.setRatio(ratio);
        return roomView;
    }
}
