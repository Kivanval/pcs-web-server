package com.example.pcswebserver.service;

import com.example.pcswebserver.data.StoreDirRepository;
import com.example.pcswebserver.data.StoreFileRepository;
import com.example.pcswebserver.data.UserRepository;
import com.example.pcswebserver.domain.StoreDir;
import com.example.pcswebserver.domain.StoreFile;
import com.example.pcswebserver.domain.StorePermissionType;
import com.example.pcswebserver.domain.User;
import com.example.pcswebserver.exception.DirNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreDirService {

    StoreFileRepository fileRepository;
    StoreDirRepository dirRepository;
    UserRepository userRepository;
    StorePermissionService permissionService;

    @Transactional
    public StoreDir create(String name, String username, UUID dirId) {
        var dir = new StoreDir();
        dir.setName(name);
        User creator = userRepository.getByUsername(username);
        dir.setCreator(creator);
        if (dirId != null) {
            var parentDir = dirRepository.findById(dirId);
            if (parentDir.isPresent()) dir.setParent(parentDir.get());
            else throw new DirNotFoundException("Dir with id %s not found".formatted(dirId));
        }
        dir = dirRepository.save(dir);
        permissionService.addPermission(dir, creator, StorePermissionType.CREATOR);
        return dir;
    }

    @Transactional
    public StoreDir create(String name, String username) {
        return create(name, username, null);
    }

    @Transactional
    public Set<StoreFile> getFiles(String username) {
        return fileRepository.findAllByCreatorUsernameAndDirIsNull(username);
    }

    @Transactional
    public Set<StoreDir> getDirs(String username) {
        return dirRepository.findAllByCreatorUsernameAndParentIsNull(username);
    }

    @Transactional
    public Set<StoreFile> getFiles(String username, UUID dirId) {
        return fileRepository.findAllByCreatorUsernameAndDirId(username, dirId);
    }

    @Transactional
    public Set<StoreDir> getDirs(String username, UUID parentId) {
        return dirRepository.findAllByCreatorUsernameAndParentId(username, parentId);
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
