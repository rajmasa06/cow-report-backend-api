package com.example.cowreport.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class CowReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String userEmail;   
    private String currentLocation;        
    private String description; 
    private String imageUrl;
    private String status = "Pending";

    // --- No-Argument Constructor (NoArgsConstructor) ---
    // यह 400 Bad Request एरर को फिक्स करने के लिए ज़रूरी है 
    // क्योंकि JSON Deserialization (जैसे Spring Boot में) को एक डिफ़ॉल्ट कंस्ट्रक्टर की आवश्यकता होती है।
    public CowReport() {
    }

    // --- All-Argument Constructor (AllArgsConstructor) ---
    public CowReport(Long id, String userName, String userEmail, String currentLocation, String description, String imageUrl, String status) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.currentLocation = currentLocation;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    // --- Getters ---
    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    // --- Setters ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- Optional: Override toString() method (Lombok's @Data includes this) ---
//    @Override
//    public String toString() {
//        return "CowReport{" +
//                "id=" + id +
//                ", userName='" + userName + '\'' +
//                ", userEmail='" + userEmail + '\'' +
//                ", currentLocation='" + currentLocation + '\'' +
//                ", description='" + description + '\'' +
//                ", imageUrl='" + imageUrl + '\'' +
//                ", status='" + status + '\'' +
//                '}';
//    }
}
//package com.example.cowreport.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//// Lombok Annotations for JSON Deserialization and JPA
//@Entity 
//@Data // Getters, Setters, toString, etc.
//@NoArgsConstructor // <-- यह annotation 400 Bad Request एरर को फिक्स करता है।
//@AllArgsConstructor
//public class CowReport {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String userName;
//    private String userEmail;   
//    private String currentlocation;        
//    private String description; 
//    private String imageUrl;
//    private String status = "Pending";
//   
//}

////package com.example.cow_report_backend.entity;
//package com.example.cowreport.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//@Entity
//public class CowReport {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    
//    private String userName;
//    private String userEmail; // User ka email (Status check ke liye)
//    private String location;
//    private String imageUrl; // Cow image ka URL ya path
//    private String status = "Pending"; // Rajmasa ka action aane tak Pending
//    
//    // Default constructor is required by JPA
//    public CowReport() {}
//
//    // --- Getters and Setters ---
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    
//    public String getUserName() { return userName; }
//    public void setUserName(String userName) { this.userName = userName; }
//    
//    public String getUserEmail() { return userEmail; }
//    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
//    
//    public String getLocation() { return location; }
//    public void setLocation(String location) { this.location = location; }
//    
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//    
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//}
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//public class CowReport {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String reporterName;
//    private String location;
//    private String status; // dead or injured
//    private String imageUrl;
//
//    private LocalDateTime reportTime = LocalDateTime.now();
//
//    // Getters and Setters
//    public Long getId() { return id; }
//    public String getReporterName() { return reporterName; }
//    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
//    public String getLocation() { return location; }
//    public void setLocation(String location) { this.location = location; }
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//    public LocalDateTime getReportTime() { return reportTime; }
//}


