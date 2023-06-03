package com.example.artvswar.util.image.roomView;


import com.example.artvswar.model.RoomView;

public interface RoomViewProvider {
    RoomView getRoom(String publicId, double paintingWidth, double paintingHeight);
}
