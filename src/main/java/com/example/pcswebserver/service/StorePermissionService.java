package com.example.pcswebserver.service;

import com.example.pcswebserver.data.StoreDirectoryPermissionRepository;
import com.example.pcswebserver.data.StoreDirectoryRepository;
import com.example.pcswebserver.data.StoreFilePermissionRepository;
import com.example.pcswebserver.domain.StoreDirectory;
import com.example.pcswebserver.domain.StoreDirectoryPermission;
import com.example.pcswebserver.domain.StorePermissionType;
import com.example.pcswebserver.domain.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StorePermissionService {

    StoreFilePermissionRepository filePermissionRepository;

    StoreDirectoryPermissionRepository dirPermissionRepository;

    public StoreDirectoryPermission addDirPermission(StoreDirectory dir, User user, StorePermissionType permissionType) {
        var dirPermission = new StoreDirectoryPermission();
        dirPermission.setDirectory(dir);
        dirPermission.setUser(user);
        dirPermission.setPermissionType(permissionType);
        return dirPermissionRepository.save(dirPermission);
    }

    @Autowired
    public void setFilePermissionRepository(StoreFilePermissionRepository filePermissionRepository) {
        this.filePermissionRepository = filePermissionRepository;
    }

    @Autowired
    public void setDiPermissionRepository(StoreDirectoryPermissionRepository dirPermissionRepository) {
        this.dirPermissionRepository = dirPermissionRepository;
    }
}
