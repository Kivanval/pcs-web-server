package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreDir;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreDirRepository
        extends CrudRepository<StoreDir, UUID> {
    Optional<StoreDir> findById(UUID id);
}
