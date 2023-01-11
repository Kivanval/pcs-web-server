package com.example.pcswebserver.service;

import com.example.pcswebserver.data.StoreDirectoryRepository;
import com.example.pcswebserver.data.StoreFileRepository;
import com.example.pcswebserver.data.UserRepository;
import com.example.pcswebserver.domain.StoreFile;
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

    @Transactional
    public StoreFile uploadFile(MultipartFile file, String username, String dirId) {
        var originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            throw new StorageException("Could not retrieve the filename");
        var filename = StringUtils.cleanPath(originalFilename);
        var storeFile = new StoreFile();
        storeFile.setName(filename);
        storeFile.setContentType(file.getContentType());
        storeFile.setSize(file.getSize());
        var dir = dirRepository.findById(UUID.fromString(dirId));
        if (dir.isPresent()) storeFile.setDirectory(dir.get());
        else throw new StoreDirectoryNotFoundException("Directory with id %s not found".formatted(dirId));
        storeFile.setCreator(userRepository.getByUsername(username));
        storeFile = fileRepository.save(storeFile);
        try {
            Files.createDirectories(Path.of(storePath));
            Path target = Path.of(storePath, storeFile.getId().toString());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return storeFile;
        } catch (IOException ex) {
            throw new StorageException("Could not store file " + originalFilename, ex);
        }
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
}
