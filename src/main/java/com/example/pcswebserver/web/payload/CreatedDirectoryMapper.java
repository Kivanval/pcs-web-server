package com.example.pcswebserver.web.payload;

import com.example.pcswebserver.domain.StoreDirectory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreatedDirectoryMapper {
    CreatedDirectoryMapper INSTANCE = Mappers.getMapper(CreatedDirectoryMapper.class);

    @Mapping(target = "creator", source = "creator.username")
    @Mapping(target = "parent", source = "parent.name")
    @Mapping(target = "createdAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    CreatedDirectory toPayload(StoreDirectory file);
}
