package com.example.artvswar.service.impl;

import com.example.artvswar.dto.response.AdminResponseDto;
import com.example.artvswar.model.Account;
import com.example.artvswar.model.Admin;
import com.example.artvswar.repository.AdminRepository;
import com.example.artvswar.service.AccountService;
import com.example.artvswar.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final AccountService accountService;

    @Override
    public List<AdminResponseDto> getAll() {
        return adminRepository.findAllBy(AdminResponseDto.class);
    }

    @Override
    @Transactional
    public Admin save(String cogitoSubject) {
        Account account = accountService.getAccountByCognitoSubject(cogitoSubject);
        String email = account.getAccountEmailData().getEmail();
        Admin admin = mapToAdmin(account, email);
        return adminRepository.save(admin);
    }

    private Admin mapToAdmin(Account account, String email) {
        Admin admin = new Admin();
        admin.setAccount(account);
        admin.setId(account.getId());
        admin.setEmail(email);
        admin.setFullName(account.getFirstName() + " " + account.getLastName());
        admin.setCognitoUsername(admin.getCognitoUsername());
        admin.setCognitoSubject(account.getCognitoSubject());
        return admin;
    }
}
