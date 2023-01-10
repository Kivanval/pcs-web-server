package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreDirectoryPermission;
import com.example.pcswebserver.domain.StoreDirectoryPermissionKey;
import com.example.pcswebserver.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface StoreDirectoryPermissionRepository
        extends CrudRepository<StoreDirectoryPermission, StoreDirectoryPermissionKey> {
    Set<StoreDirectoryPermission> findAllByUser(User user);
}
