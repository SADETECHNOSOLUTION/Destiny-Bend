package com.sadetech.profession.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);  // Initialize logger
    private static final String uploadDir = "static/uploads/";

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            logger.warn("Received an empty file for upload");
            throw new IllegalArgumentException("File is empty");
        }

        try {
            // Generate a unique file name based on UUID
            String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path uploadPath = Paths.get(uploadDir);

            // Ensure the directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created upload directory at: {}", uploadDir);
            }

            // Resolve the file path
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File uploaded successfully: {}", fileName);

            // Return file path or URL (depending on how files are served)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            logger.error("Error occurred while uploading file: {}", file.getOriginalFilename(), e);
            throw new IOException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }
}