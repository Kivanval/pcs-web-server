package com.example.pcswebserver.web.payload;

import com.example.pcswebserver.domain.StoreDir;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreatedDirMapper {
    CreatedDirMapper INSTANCE = Mappers.getMapper(CreatedDirMapper.class);

    @Mapping(target = "creator", source = "creator.username")
    @Mapping(target = "parent", source = "parent.name")
    @Mapping(target = "createdAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    CreatedDir toPayload(StoreDir file);
}
