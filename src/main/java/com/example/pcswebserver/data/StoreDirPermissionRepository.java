package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreDirPermission;
import com.example.pcswebserver.domain.StoreDirPermissionKey;
import com.example.pcswebserver.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StoreDirPermissionRepository
        extends CrudRepository<StoreDirPermission, StoreDirPermissionKey> {
    Set<StoreDirPermission> findAllByUser(User user);
}
