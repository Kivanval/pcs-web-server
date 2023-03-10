package com.example.pcswebserver.service.impl;

import com.example.pcswebserver.data.StoreDirRepository;
import com.example.pcswebserver.data.StoreFileRepository;
import com.example.pcswebserver.data.UserRepository;
import com.example.pcswebserver.domain.StoreFile;
import com.example.pcswebserver.domain.StorePermissionType;
import com.example.pcswebserver.domain.User;
import com.example.pcswebserver.exception.DirNotFoundException;
import com.example.pcswebserver.exception.FileNotFoundException;
import com.example.pcswebserver.exception.StorageException;
import com.example.pcswebserver.service.StoreFileService;
import com.example.pcswebserver.service.StorePermissionService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreFileServiceImpl implements StoreFileService {
    @Value("${store.path}")
    String storePathUrl;
    StoreFileRepository fileRepository;
    StoreDirRepository dirRepository;
    UserRepository userRepository;
    StorePermissionService permissionService;

    @Transactional
    @Override
    public Resource load(UUID id) {
        try {
            return new UrlResource(Path.of(storePathUrl, id.toString()).toUri());
        } catch (MalformedURLException e) {
            throw new StorageException("Could not load file", e);
        }
    }

    @Transactional
    @Override
    public StoreFile getById(UUID id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File %s not found".formatted(id)));
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        try {
            Files.delete(Path.of(storePathUrl, id.toString()));
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + id, ex);
        }
        fileRepository.deleteById(id);
    }

    @Transactional
    @Override
    public StoreFile upload(MultipartFile file, String username, UUID dirId) {
        return create(file.getOriginalFilename(), username, file.getSize(), dirId, storeFile -> store(file, storeFile));
    }

    @Transactional
    @Override
    public StoreFile upload(MultipartFile file, String username) {
        return upload(file, username, null);
    }


    private StoreFile create(String name, String username, long size, UUID dirId, Consumer<StoreFile> fileConsumer) {
        var storeFile = new StoreFile();
        storeFile.setName(name);
        storeFile.setSize(size);
        User creator = userRepository.getByUsername(username);
        storeFile.setCreator(creator);
        if (dirId != null) {
            var dir = dirRepository.findById(dirId);
            if (dir.isPresent()) storeFile.setDir(dir.get());
            else throw new DirNotFoundException("Dir %s not found".formatted(dirId));
        }
        storeFile = fileRepository.save(storeFile);
        permissionService.addPermission(storeFile, creator, StorePermissionType.CREATOR);
        fileConsumer.accept(storeFile);
        return storeFile;
    }

    @Transactional
    @Override
    public StoreFile create(String name, String username) {
        return create(name, username, null);
    }

    @Transactional
    @Override
    public StoreFile create(String name, String username, UUID dirId) {
        return create(name, username, 0, dirId, this::store);
    }

    private StoreFile store(MultipartFile file, StoreFile storeFile) {
        try {
            Files.copy(file.getInputStream(), getTarget(storeFile), StandardCopyOption.REPLACE_EXISTING);
            return storeFile;
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + storeFile.getName(), ex);
        }
    }

    private StoreFile store(StoreFile storeFile) {
        try {
            Files.createFile(getTarget(storeFile));
            return storeFile;
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + storeFile.getName(), ex);
        }
    }


    private Path getTarget(StoreFile storeFile) throws IOException {
        var storePath = Path.of(storePathUrl);
        Files.createDirectories(storePath);
        return storePath.resolve(storeFile.getId().toString());
    }

    @Autowired
    public void setFileRepository(StoreFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    public void setDirRepository(StoreDirRepository dirRepository) {
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
