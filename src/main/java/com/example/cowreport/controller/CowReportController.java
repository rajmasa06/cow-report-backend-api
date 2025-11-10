package com.example.cowreport.controller;

import com.example.cowreport.model.CowReport;
import com.example.cowreport.service.CowReportService;
import com.example.cowreport.service.CloudinaryImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cow")
@CrossOrigin("*")
public class CowReportController {

    @Autowired
    private CowReportService cowReportService;

    @Autowired
    private CloudinaryImageService cloudinaryImageService; // ✅ new Cloud service

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1️⃣ Submit Report (Cloud upload)
    @PostMapping("/submit")
    public ResponseEntity<String> submitReport(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("reportData") String reportDataJson) {
        try {
            // JSON string → CowReport object
            CowReport report = objectMapper.readValue(reportDataJson, CowReport.class);

            // ✅ Cloudinary pe upload karo aur URL lo
            String imageUrl = cloudinaryImageService.uploadImage(imageFile);
            report.setImageUrl(imageUrl);
            report.setStatus("Pending");

            // ✅ Save to DB
            CowReport savedReport = cowReportService.saveReport(report);

            // ✅ Email bhejna
            cowReportService.sendNewReportAlert(savedReport);

            return new ResponseEntity<>(
                    "Report submitted successfully! Report ID: " + savedReport.getId(),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error while submitting report: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ 2️⃣ Rajmasa ke liye update API
    @GetMapping("/status/update/{reportId}")
    public ResponseEntity<String> updateStatusToDone(@PathVariable Long reportId) {
        try {
            cowReportService.updateReportStatus(reportId, "Done");
            return ResponseEntity.ok("Report ID " + reportId + " successfully marked as DONE. Thank you!");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ✅ 3️⃣ User-specific reports (Android)
    @GetMapping("/myreports/{userEmail}")
    public ResponseEntity<List<CowReport>> getUserReports(@PathVariable String userEmail) {
        List<CowReport> reports = cowReportService.getReportsByEmail(userEmail);
        if (reports.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reports);
    }

    // ✅ 4️⃣ Get all reports (Rajmasa Dashboard)
    @GetMapping("/all")
    public ResponseEntity<List<CowReport>> getAllReports() {
        List<CowReport> allReports = cowReportService.getAllReports();
        return ResponseEntity.ok(allReports);
    }

    // ✅ 5️⃣ Mark report done (Android)
    @PutMapping("/mark-done/{reportId}")
    public ResponseEntity<String> markReportDoneByAndroid(@PathVariable Long reportId) {
        try {
            cowReportService.updateReportStatus(reportId, "Done");
            return ResponseEntity.ok("Report ID " + reportId + " successfully marked as DONE via Android App.");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
