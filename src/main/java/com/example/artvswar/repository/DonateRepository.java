package com.example.artvswar.repository;

import com.example.artvswar.model.Donate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonateRepository extends JpaRepository<Donate, Long> {
}
