package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.AdminResponseDto;
import com.example.artvswar.repository.AdminRepository;
import com.example.artvswar.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    @Override
    public List<AdminResponseDto> getAll() {
        return adminRepository.findAllBy(AdminResponseDto.class);
    }
}
