package com.example.pcswebserver.web.payload.mapper;

import com.example.pcswebserver.domain.StoreDir;
import com.example.pcswebserver.domain.StoreFile;
import com.example.pcswebserver.web.payload.OpenedDir;

import java.util.Set;
import java.util.stream.Collectors;

public class OpenedDirMapperImpl implements OpenedDirMapper {
    @Override
    public OpenedDir toPayload(Set<StoreDir> dirs, Set<StoreFile> files) {
        var dir = new OpenedDir();
        dir.setDirs(dirs.stream().map(CreatedDirMapper.INSTANCE::toPayload).collect(Collectors.toSet()));
        dir.setFiles(files.stream().map(CreatedFileMapper.INSTANCE::toPayload).collect(Collectors.toSet()));
        return dir;
    }
}
