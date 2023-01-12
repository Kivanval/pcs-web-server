package com.example.pcswebserver.web.payload.mapper;

import com.example.pcswebserver.domain.StoreFile;
import com.example.pcswebserver.web.payload.CreatedFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreatedFileMapper {
    CreatedFileMapper INSTANCE = Mappers.getMapper(CreatedFileMapper.class);

    @Mapping(target = "creator", source = "creator.username")
    @Mapping(target = "dir", source = "dir.name")
    @Mapping(target = "createdAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    CreatedFile toPayload(StoreFile file);
}