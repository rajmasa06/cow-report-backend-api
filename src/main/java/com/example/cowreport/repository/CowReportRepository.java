package com.example.cowreport.repository;

import com.example.cowreport.model.CowReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CowReportRepository extends JpaRepository<CowReport, Long> {
    
    // Android ke 'Get Data' feature ke liye
    List<CowReport> findByUserEmail(String userEmail);
    Optional<CowReport> findTopByUserEmailOrderByIdDesc(String userEmail);
}