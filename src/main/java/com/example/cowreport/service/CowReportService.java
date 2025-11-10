package com.example.cowreport.service;
import com.example.cowreport.model.CowReport;
import com.example.cowreport.repository.CowReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CowReportService {

    @Autowired
    private CowReportRepository cowReportRepository;

    @Autowired(required = false)
    private EmailService emailService; 

    
//      1️⃣ Save a new report in DB
     
    public CowReport saveReport(CowReport report) {
        CowReport saved = cowReportRepository.save(report);

        // Email alert (only if EmailService bean is available)
        if (emailService != null) {
            try {
                emailService.sendReportAlert(saved);
            } catch (Exception e) {
                System.err.println("⚠️ Email sending failed: " + e.getMessage());
            }
        }

        return saved;
    }

  
    //      2️⃣ Send alert for a new report (optional extra call from controller)
     
    public void sendNewReportAlert(CowReport report) {
        if (emailService != null) {
            try {
                emailService.sendReportAlert(report);
            } catch (Exception e) {
                System.err.println("⚠️ Failed to send alert email: " + e.getMessage());
            }
        } else {
            System.out.println("📩 EmailService not configured. Skipping email alert...");
        }
    }

   
//      Update report status (used for Rajmasa action and Android PUT)
 
    public void updateReportStatus(Long reportId, String newStatus) {
        Optional<CowReport> optionalReport = cowReportRepository.findById(reportId);
        if (optionalReport.isPresent()) {
            CowReport report = optionalReport.get();
            report.setStatus(newStatus);
            cowReportRepository.save(report);
            System.out.println("✅ Report " + reportId + " marked as " + newStatus);
        } else {
            throw new RuntimeException("Report not found with ID: " + reportId);
        }
    }

   
    //      4️⃣ Fetch reports for a user (used in Android app)
    
    public List<CowReport> getReportsByEmail(String userEmail) {
        // Assuming CowReportRepository has findByUserEmail method
        return cowReportRepository.findByUserEmail(userEmail); 
    }

   
//      Fetch ALL reports (Used in Android app for Admin/All View)
     
    public List<CowReport> getAllReports() {
        // JpaRepository का findAll() method DB से सभी रिपोर्ट्स लाता है
        return cowReportRepository.findAll(); 
    }
}
