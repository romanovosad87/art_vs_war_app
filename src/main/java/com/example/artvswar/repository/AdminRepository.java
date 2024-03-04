package com.example.artvswar.repository;

import com.example.artvswar.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    <T> List<T> findAllBy(Class<T> type);
}
