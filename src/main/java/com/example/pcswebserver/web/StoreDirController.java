package com.example.pcswebserver.web;

import com.example.pcswebserver.service.StoreDirService;
import com.example.pcswebserver.web.payload.CreateDir;
import com.example.pcswebserver.web.payload.CreatedDir;
import com.example.pcswebserver.web.payload.OpenedDir;
import com.example.pcswebserver.web.payload.mapper.CreatedDirMapper;
import com.example.pcswebserver.web.payload.mapper.OpenedDirMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.pcswebserver.web.WebConstants.*;

@RestController
@RequestMapping(STORE + DIR)
@SecurityRequirement(name = "pcs-api")
public class StoreDirController {
    private StoreDirService storeService;

    @GetMapping(OPEN)
    @ResponseStatus(HttpStatus.OK)
    public OpenedDir open(Authentication auth) {
        return OpenedDirMapper.INSTANCE
                .toPayload(
                        storeService.getDirs(auth.getName()),
                        storeService.getFiles(auth.getName())
                );
    }

    @GetMapping(OPEN + "/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public OpenedDir open(@PathVariable("dir-id") UUID dirId,
                          Authentication auth) {
        return OpenedDirMapper.INSTANCE
                .toPayload(
                        storeService.getDirs(auth.getName(), dirId),
                        storeService.getFiles(auth.getName(), dirId)
                );
    }

    @PostMapping(CREATE)
    @ResponseStatus(HttpStatus.OK)
    public CreatedDir create(@RequestBody CreateDir dir,
                             Authentication auth) {
        return CreatedDirMapper.INSTANCE
                .toPayload(storeService
                        .create(dir.getName(), auth.getName()));
    }

    @PostMapping(CREATE + "/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatedDir create(@RequestBody CreateDir dir,
                             @PathVariable("dir-id") UUID dirId,
                             Authentication auth) {
        return CreatedDirMapper.INSTANCE
                .toPayload(storeService
                        .create(dir.getName(), auth.getName(), dirId));
    }

    @DeleteMapping(DELETE + "/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("dir-id") UUID dirId) {
        storeService.delete(dirId);
    }

    @Autowired
    public void setStoreService(StoreDirService storeService) {
        this.storeService = storeService;
    }
}