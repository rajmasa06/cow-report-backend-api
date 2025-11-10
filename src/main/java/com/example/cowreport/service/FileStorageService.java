package com.example.cowreport.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.IOException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("./uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory for file storage.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            // 🔹 Yeh aapka BASE URL hai (deployment ke hisab se badlega)
            String baseUrl = "http://localhost:8080"; // local testing ke liye
            // agar deploy karoge to likhna: "http://your-public-ip:8080"

            // 🔹 Final URL jo email me clickable hoga
            return baseUrl + "/uploads/" + fileName;

        } catch (IOException ex) {
            throw new RuntimeException("File storage failed for " + fileName + ": " + ex.getMessage(), ex);
        }
    }
}
