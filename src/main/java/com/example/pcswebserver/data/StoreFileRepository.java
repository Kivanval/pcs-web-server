package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreFileRepository
        extends CrudRepository<StoreFile, UUID> {
}
