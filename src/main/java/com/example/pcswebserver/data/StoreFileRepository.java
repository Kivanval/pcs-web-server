package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreFile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface StoreFileRepository
        extends CrudRepository<StoreFile, UUID> {
}
