package com.example.artvswar.service;

import java.util.Map;

public interface ImageService {
    String generateGetUrl(String fileName);

    Map<String, String> generatePutUrl(String extension);

    void delete(String fileName);
}
