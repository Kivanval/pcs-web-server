package com.example.pcswebserver.service;

import com.example.pcswebserver.domain.*;

public interface StorePermissionService {

    StoreDirPermission addPermission(StoreDir dir, User user, StorePermissionType permissionType);

    StoreFilePermission addPermission(StoreFile file, User user, StorePermissionType permissionType);
}
