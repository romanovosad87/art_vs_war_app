package com.example.artvswar.service;

import com.example.artvswar.dto.response.AdminResponseDto;
import java.util.List;

public interface AdminService {
    List<AdminResponseDto> getAll();
}
