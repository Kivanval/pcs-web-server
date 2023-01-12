package com.example.pcswebserver.web.payload.mapper;

import com.example.pcswebserver.domain.StoreDir;
import com.example.pcswebserver.domain.StoreFile;
import com.example.pcswebserver.web.payload.OpenedDir;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

public interface OpenedDirMapper {
    OpenedDirMapper INSTANCE = new OpenedDirMapperImpl();

    OpenedDir toPayload(Set<StoreDir> dirs, Set<StoreFile> files);
}
