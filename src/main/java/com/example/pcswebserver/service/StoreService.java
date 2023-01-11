package com.example.pcswebserver.service;

import com.example.pcswebserver.data.StoreDirectoryRepository;
import com.example.pcswebserver.data.StoreFileRepository;
import com.example.pcswebserver.data.UserRepository;
import com.example.pcswebserver.domain.StoreDirectory;
import com.example.pcswebserver.domain.StoreFile;
import com.example.pcswebserver.domain.StorePermissionType;
import com.example.pcswebserver.domain.User;
import com.example.pcswebserver.exception.StorageException;
import com.example.pcswebserver.exception.StoreDirectoryNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreService {
    @Value("${store.path}")
    String storePath;
    StoreFileRepository fileRepository;
    StoreDirectoryRepository dirRepository;
    UserRepository userRepository;
    StorePermissionService permissionService;

    @Transactional
    public StoreFile store(MultipartFile file, StoreFile storeFile) {
        try {
            var targetDir = Path.of(storePath, storeFile.getId().toString());
            Files.createDirectories(targetDir);
            var target = Path.of(targetDir.toString(), storeFile.getName());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return storeFile;
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + storeFile.getName(), ex);
        }
    }

    @Transactional
    public StoreFile create(MultipartFile file, String username, UUID dirId) {
        var originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            throw new StorageException("Could not retrieve the filename");
        var filename = StringUtils.cleanPath(originalFilename);
        var storeFile = new StoreFile();
        storeFile.setName(filename);
        storeFile.setContentType(file.getContentType());
        storeFile.setSize(file.getSize());
        if (dirId != null) {
            var dir = dirRepository.findById(dirId);
            if (dir.isPresent()) storeFile.setDirectory(dir.get());
            else throw new StoreDirectoryNotFoundException("Directory with id %s not found".formatted(dirId));
        }
        storeFile.setCreator(userRepository.getByUsername(username));
        return fileRepository.save(storeFile);
    }

    @Transactional
    public StoreFile create(MultipartFile file, String username) {
        return create(file, username, null);
    }

    @Transactional
    public StoreDirectory create(String name, String username) {
        return create(name, username, null);
    }

    @Transactional
    public StoreDirectory create(String name, String username, UUID dirId) {
        var dir = new StoreDirectory();
        dir.setName(name);
        User creator = userRepository.getByUsername(username);
        dir.setCreator(creator);
        if (dirId != null) {
            var parentDir = dirRepository.findById(dirId);
            if (parentDir.isPresent()) dir.setParent(parentDir.get());
            else throw new StoreDirectoryNotFoundException("Directory with id %s not found".formatted(dirId));
        }
        dir = dirRepository.save(dir);
        permissionService.addDirPermission(dir, creator, StorePermissionType.CREATOR);
        return dir;
    }

    @Autowired
    public void setFileRepository(StoreFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    public void setDirRepository(StoreDirectoryRepository dirRepository) {
        this.dirRepository = dirRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPermissionService(StorePermissionService permissionService) {
        this.permissionService = permissionService;
    }
}
