package com.Sadetechno.post_module.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    private final String uploadDir = "static/uploads/";

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);



    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try {
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String encodedFileName = encodeFileName(fileName);
            String fileUrl = "/uploads/" + encodedFileName;

            logger.info("File uploaded successfully. URL: {}", fileUrl);

            return fileUrl;
        } catch (IOException e) {
            logger.error("Could not store file " + file.getOriginalFilename(), e);
            throw new IOException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }

    private String encodeFileName(String fileName) {
        String[] parts = fileName.split(" at ");
        if (parts.length == 2) {
            String firstPart = URLEncoder.encode(parts[0], StandardCharsets.UTF_8).replace("+", "%20");
            String secondPart = URLEncoder.encode(parts[1], StandardCharsets.UTF_8).replace("+", "%20");
            return firstPart + " at%20" + secondPart;  // Note the space before "at" is not encoded
        } else {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        }
    }

    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl != null) {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            Path filePath = Paths.get(uploadDir + fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new IOException("File not found: " + filePath.toString());
            }
        }
    }

}

