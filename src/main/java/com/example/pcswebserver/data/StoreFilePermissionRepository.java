package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreFilePermission;
import com.example.pcswebserver.domain.StoreFilePermissionKey;
import com.example.pcswebserver.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StoreFilePermissionRepository
        extends CrudRepository<StoreFilePermission, StoreFilePermissionKey> {
    Set<StoreFilePermission> findAllByUser(User user);
}
