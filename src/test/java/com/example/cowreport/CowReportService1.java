package com.example.cowreport;

import com.example.cowreport.model.CowReport;

/**
 * यह इंटरफेस CowReportServiceImpl के लिए सभी बिज़नेस लॉजिक मेथड्स को
 * परिभाषित (Define) करता है।
 */
public interface CowReportService1{
    
    // रिपोर्ट को डेटाबेस में सेव करने का मेथड
    CowReport saveReport(CowReport report);
    
    // Rajmasa को नई रिपोर्ट की चेतावनी भेजने का मेथड
    void sendNewReportAlert(CowReport report);
    
    // रिपोर्ट का स्टेटस (Pending/Done) अपडेट करने का मेथड
    CowReport updateReportStatus(Long id, String status);

    /**
     * उपयोगकर्ता के ईमेल द्वारा नवीनतम रिपोर्ट प्राप्त करने का मेथड।
     * यह मेथड आपकी CowReportController की एरर को ठीक करता है।
     */
    CowReport getLatestReportByEmail(String email);
}

