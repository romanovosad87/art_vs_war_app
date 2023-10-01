package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.response.image.RoomViewResponseDto;
import com.example.artvswar.model.RoomView;
import org.springframework.stereotype.Component;

@Component
public class RoomViewMapper {

    public RoomViewResponseDto toRoomViewResponseDto(RoomView roomView) {
        RoomViewResponseDto dto = new RoomViewResponseDto();
        dto.setImageUrl(roomView.getImageUrl());
        return dto;
    }
}
