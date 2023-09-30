package com.example.artvswar.controller;

import com.example.artvswar.dto.response.MainPageDataResponseDto;
import com.example.artvswar.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mainPage")
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;

    @GetMapping("/data")
    public MainPageDataResponseDto getDataForMainPage() {
        return mainPageService.getDataForMainPage();
    }
}
