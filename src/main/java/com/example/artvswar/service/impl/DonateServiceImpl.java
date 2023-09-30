package com.example.artvswar.service.impl;

import com.example.artvswar.model.Donate;
import com.example.artvswar.repository.DonateRepository;
import com.example.artvswar.service.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DonateServiceImpl implements DonateService {
    private final DonateRepository donateRepository;
    @Override
    @Transactional
    public Donate save(Donate donate) {

        return donateRepository.save(donate);
    }
}
