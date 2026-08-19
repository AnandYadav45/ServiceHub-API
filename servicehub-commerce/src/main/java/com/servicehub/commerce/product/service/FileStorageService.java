package com.servicehub.commerce.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String store(MultipartFile file, String subFolder);
    void delete(String relativePath);
}
