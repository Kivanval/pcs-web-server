package com.example.pcswebserver.service;

import com.example.pcswebserver.data.StoreDirPermissionRepository;
import com.example.pcswebserver.data.StoreFilePermissionRepository;
import com.example.pcswebserver.domain.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StorePermissionService {

    StoreFilePermissionRepository filePermissionRepository;

    StoreDirPermissionRepository dirPermissionRepository;

    public StoreDirPermission addPermission(StoreDir dir, User user, StorePermissionType permissionType) {
        var dirPermission = new StoreDirPermission();
        dirPermission.setDir(dir);
        dirPermission.setUser(user);
        dirPermission.setPermissionType(permissionType);
        return dirPermissionRepository.save(dirPermission);
    }

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
