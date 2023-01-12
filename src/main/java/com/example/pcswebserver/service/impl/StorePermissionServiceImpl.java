package com.example.pcswebserver.service.impl;

import com.example.pcswebserver.data.StoreDirPermissionRepository;
import com.example.pcswebserver.data.StoreFilePermissionRepository;
import com.example.pcswebserver.domain.*;
import com.example.pcswebserver.service.StorePermissionService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StorePermissionServiceImpl implements StorePermissionService {

    StoreFilePermissionRepository filePermissionRepository;

    StoreDirPermissionRepository dirPermissionRepository;

    @Transactional
    @Override
    public StoreDirPermission addPermission(StoreDir dir, User user, StorePermissionType permissionType) {
        var dirPermission = new StoreDirPermission();
        dirPermission.setDir(dir);
        dirPermission.setUser(user);
        dirPermission.setPermissionType(permissionType);
        return dirPermissionRepository.save(dirPermission);
    }

    @Transactional
    @Override
    public StoreFilePermission addPermission(StoreFile file, User user, StorePermissionType permissionType) {
        var filePermission = new StoreFilePermission();
        filePermission.setFile(file);
        filePermission.setUser(user);
        filePermission.setPermissionType(permissionType);
        return filePermissionRepository.save(filePermission);
    }

    @Autowired
    public void setFilePermissionRepository(StoreFilePermissionRepository filePermissionRepository) {
        this.filePermissionRepository = filePermissionRepository;
    }

    @Autowired
    public void setDiPermissionRepository(StoreDirPermissionRepository dirPermissionRepository) {
        this.dirPermissionRepository = dirPermissionRepository;
    }
}
