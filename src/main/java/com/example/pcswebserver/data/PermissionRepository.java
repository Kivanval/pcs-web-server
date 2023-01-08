package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository
        extends CrudRepository<Permission, Long> {
    Optional<Permission> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);
}