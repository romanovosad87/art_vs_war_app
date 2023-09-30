package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.MainPageDataResponseDto;
import com.example.artvswar.repository.painting.PaintingRepository;
import com.example.artvswar.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
    private final PaintingRepository paintingRepository;
    @Override
    public MainPageDataResponseDto getDataForMainPage() {
        return paintingRepository.getDataForMainPage();
    }
}
