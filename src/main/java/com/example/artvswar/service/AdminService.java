package com.example.artvswar.service;

import com.example.artvswar.dto.response.AdminResponseDto;
import com.example.artvswar.model.Admin;
import java.util.List;

public interface AdminService {
    List<AdminResponseDto> getAll();
    Admin save(String cogitoSubject);
}
