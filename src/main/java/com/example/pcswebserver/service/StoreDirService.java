package com.example.pcswebserver.service;

import com.example.pcswebserver.domain.StoreDir;
import com.example.pcswebserver.domain.StoreFile;

import java.util.Set;
import java.util.UUID;

public interface StoreDirService {
    StoreDir create(String name, String username, UUID dirId);
    StoreDir create(String name, String username);
    void delete(UUID id);
    Set<StoreFile> getFiles(String username);
    Set<StoreDir> getDirs(String username);
    Set<StoreFile> getFiles(String username, UUID dirId);
    Set<StoreDir> getDirs(String username, UUID parentId);
}
