package com.servicehub.commerce.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileStorageServiceImpl implements FileStorageService{
    private final Path uploadRoot;

    @Value("${app.storage.upload-dir}")
    private String UploadDir;

    public FileStorageServiceImpl(Path uploadRoot) {
        this.uploadRoot = Paths.get(UploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload directory: " + uploadRoot, e);
        }

    }

    @Override
    public String store(MultipartFile file, String subFolder) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String extension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
        // Never trust the client's filename for the stored name — that's the classic path-traversal vector
        String storedFilename = UUID.randomUUID() + extension;

        Path targetDir = uploadRoot.resolve(subFolder).normalize();
        if (!targetDir.startsWith(uploadRoot)) {
            throw new IllegalArgumentException("Invalid storage path");
        }

        try {
            Files.createDirectories(targetDir);
            Path targetFile = targetDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file", e);
        }

        return subFolder + "/" + storedFilename;
    }

    @Override
    public void delete(String relativePath) {
        Path file = uploadRoot.resolve(relativePath).normalize();
        if (!file.startsWith(uploadRoot)) {
            throw new IllegalArgumentException("Invalid file path");
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete file", e);
        }
    }
}
