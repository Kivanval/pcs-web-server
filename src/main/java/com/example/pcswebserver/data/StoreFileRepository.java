package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.StoreFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface StoreFileRepository
        extends CrudRepository<StoreFile, UUID> {
    Set<StoreFile> findAllByCreatorUsernameAndDirIsNull(String username);

    Set<StoreFile> findAllByCreatorUsernameAndDirId(String username, UUID dirId);
}
