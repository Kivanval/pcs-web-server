package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreDirectoryPermission;
import com.example.pcswebserver.domain.StoreDirectoryPermissionKey;
import com.example.pcswebserver.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StoreDirectoryPermissionRepository
        extends CrudRepository<StoreDirectoryPermission, StoreDirectoryPermissionKey> {
    Set<StoreDirectoryPermission> findAllByUser(User user);
}
