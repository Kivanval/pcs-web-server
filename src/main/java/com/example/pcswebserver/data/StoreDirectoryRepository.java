package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreDirectory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreDirectoryRepository
        extends CrudRepository<StoreDirectory, UUID> {
    Optional<StoreDirectory> findById(UUID id);
}
