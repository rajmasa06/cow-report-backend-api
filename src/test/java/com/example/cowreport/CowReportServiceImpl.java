package com.example.cowreport;

import com.example.cowreport.model.CowReport;
import com.example.cowreport.repository.CowReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CowReportServiceImpl implements CowReportService1{

    private final CowReportRepository cowReportRepository;
    private final JavaMailSender mailSender;

    // Rajmasa's email to receive alerts
    @Value("${cow.report.recipient.email:rajmasa06@gmail.com}") 
    private String recipientEmail;

    @Autowired
    public CowReportServiceImpl(CowReportRepository cowReportRepository, JavaMailSender mailSender) {
        this.cowReportRepository = cowReportRepository;
        this.mailSender = mailSender;
    }

    @Override
    public CowReport saveReport(CowReport report) {
        return cowReportRepository.save(report);
    }

    @Override
    public void sendNewReportAlert(CowReport report) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_spring_email@gmail.com"); 
        message.setTo(recipientEmail);
        message.setSubject("URGENT: New Cow Report - ID: " + report.getId());
        
        String body = String.format(
            "Dear Rajmasa,\n\nA **new cow report** has been submitted by a user.\n\n" +
            "--- REPORT DETAILS ---\n" +
            "Report ID: %d\n" +
            "Submitted By: **%s**\n" +
            "User Contact: %s\n" +
            "Location: %s\n" +
            "Description: %s\n" +
            "Image Link: http://localhost:8080%s\n\n" +
            "--- ACTION REQUIRED ---\n" +
            "After cleaning, please click the link below to mark this report as **DONE**:\n" +
            "http://localhost:8080/api/cow/status/update/%d\n",
            report.getId(),
            report.getUserName(),
            report.getUserEmail(),
            report.getCurrentLocation() != null ? report.getCurrentLocation() : "N/A",
            report.getDescription(),
            report.getImageUrl(),
            report.getId()
        );
        message.setText(body);
        mailSender.send(message);
    }
    
    @Override
    public CowReport updateReportStatus(Long id, String status) {
        return cowReportRepository.findById(id)
                .map(report -> {
                    report.setStatus(status);
                    return cowReportRepository.save(report);
                })
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + id));
    }

    /**
     * Finds the latest submitted cow report by a specific user email.
     * यह method Controller की एरर को ठीक करता है।
     */
    @Override
    public CowReport getLatestReportByEmail(String email) {
        // Repository method का उपयोग करके नवीनतम रिपोर्ट ढूंढें
        Optional<CowReport> latestReport = cowReportRepository.findTopByUserEmailOrderByIdDesc(email);
        
        // यदि रिपोर्ट नहीं मिलती है, तो RuntimeException फेंकें
        return latestReport.orElseThrow(() -> new RuntimeException("No latest report found for user email: " + email));
    }
}

