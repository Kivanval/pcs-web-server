package com.example.pcswebserver.web.payload;


import com.example.pcswebserver.domain.StoreFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UploadedFileMapper {
    UploadedFileMapper INSTANCE = Mappers.getMapper(UploadedFileMapper.class);

    @Mapping(target = "creator", source = "creator.username")
    @Mapping(target = "dir", source = "directory.name")
    @Mapping(target = "createdAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    UploadedFile toPayload(StoreFile file);
}
