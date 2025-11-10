package com.example.cowreport.controller;
import com.example.cowreport.service.FileStorageService; // File storage service का इंपोर्ट
import com.fasterxml.jackson.databind.ObjectMapper;       // ObjectMapper का इंपोर्ट
import com.example.cowreport.model.CowReport;
import com.example.cowreport.service.CowReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/cow")
@CrossOrigin("*") // Android se request allow karne ke liye
public class CowReportController { // <-- Class Start

    @Autowired
    private CowReportService cowReportService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    
//      POST API: /api/cow/submit
     
    @PostMapping("/submit")
    public ResponseEntity<String> submitReport(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("reportData") String reportDataJson) {
        // ... (submitReport logic) ...
        try {
             CowReport report = objectMapper.readValue(reportDataJson, CowReport.class);
             String imageUrl = fileStorageService.storeFile(imageFile);
             report.setImageUrl(imageUrl);
             report.setStatus("Pending");
             CowReport savedReport = cowReportService.saveReport(report);
             cowReportService.sendNewReportAlert(savedReport);
             return new ResponseEntity<>(
                     "Report submitted successfully! Report ID: " + savedReport.getId(),
                     HttpStatus.CREATED
             );
        } catch (Exception e) {
             e.printStackTrace();
             return new ResponseEntity<>("Error while submitting report.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
//         GET API: Rajmasa link /api/cow/status/update/{reportId}
     
    @GetMapping("/status/update/{reportId}")
    public ResponseEntity<String> updateStatusToDone(@PathVariable Long reportId) {
        try {
            cowReportService.updateReportStatus(reportId, "Done");
            return ResponseEntity.ok("Report ID " + reportId + " successfully marked as DONE. Thank you!");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

   
//    3️⃣ GET API: Android App /api/cow/myreports/{userEmail}
    @GetMapping("/myreports/{userEmail}")
    public ResponseEntity<List<CowReport>> getUserReports(@PathVariable String userEmail) {
        List<CowReport> reports = cowReportService.getReportsByEmail(userEmail);
        if (reports.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reports);
    }
    
    
//     * 4️⃣ GET API: Android App /api/cow/all)
     
    @GetMapping("/all") 
    public ResponseEntity<List<CowReport>> getAllReports() {
        List<CowReport> allReports = cowReportService.getAllReports(); 
        if (allReports.isEmpty()) {
            return ResponseEntity.ok(allReports); 
        }
        return ResponseEntity.ok(allReports);
    }
    
    // ✅ 5️⃣ PUT API: Android App Mark as Done /api/cow/mark-done/{reportId}
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
