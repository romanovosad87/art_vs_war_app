package com.example.artvswar.service.impl;

import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.repository.MockRoomRepository;
import com.example.artvswar.service.MockRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MockRoomServiceImpl implements MockRoomService {

    private final MockRoomRepository mockRoomRepository;
    @Override
    public String getPublicId(Long id) {
        return mockRoomRepository.findById(id).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find mock room by id: %s", id)))
                .getPublicId();
    }
}
