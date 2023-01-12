package com.example.pcswebserver.service;

import com.example.pcswebserver.domain.StoreFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StoreFileService {
    Resource load(UUID id);

    StoreFile getById(UUID id);

    void delete(UUID id);

    StoreFile upload(MultipartFile file, String username);

    StoreFile upload(MultipartFile file, String username, UUID dirId);

    StoreFile create(String name, String username);

    StoreFile create(String name, String username, UUID dirId);
}
