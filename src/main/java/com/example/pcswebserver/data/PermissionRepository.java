package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.FilePermission;
import com.example.pcswebserver.domain.FilePermissionKey;
import com.example.pcswebserver.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PermissionRepository
        extends CrudRepository<FilePermission, FilePermissionKey> {
    Optional<FilePermission> findByUser(User user);

}
