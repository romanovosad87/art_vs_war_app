package com.example.artvswar.controller;

import com.example.artvswar.dto.request.admin.AdminRequestDto;
import com.example.artvswar.model.Admin;
import com.example.artvswar.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Long> createAdmin(@RequestBody AdminRequestDto dto) {
        Admin admin = adminService.save(dto.getCognitoSubject());
        Long id = admin.getId();
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
