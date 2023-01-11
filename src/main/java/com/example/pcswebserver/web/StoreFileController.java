package com.example.pcswebserver.web;

import com.example.pcswebserver.service.StoreFileService;
import com.example.pcswebserver.web.payload.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.example.pcswebserver.web.WebConstants.*;

@RestController
@RequestMapping(STORE + FILE)
public class StoreFileController {
    private StoreFileService storeService;

    @PostMapping(UPLOAD)
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile upload(@RequestParam("file") MultipartFile file,
                               Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService
                        .store(file, storeService
                                .save(file, auth.getName())));
    }

    @PostMapping(UPLOAD + "/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile upload(@RequestParam("file") MultipartFile file,
                               @PathVariable("dir-id") UUID dirId,
                               Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService
                        .store(file, storeService
                                .save(file, auth.getName(), dirId)));
    }

    @PostMapping(CREATE)
    @ResponseStatus(HttpStatus.OK)
    public CreatedFile create(@RequestBody @Valid CreateFile file,
                              Authentication auth) {
        return CreatedFileMapper.INSTANCE
                .toPayload(storeService
                        .store(storeService
                                .save(file.getName(), auth.getName())));
    }

    @PostMapping(CREATE + "/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatedFile create(@RequestBody @Valid CreateFile file,
                              @PathVariable("dir-id") UUID dirId,
                              Authentication auth) {
        return CreatedFileMapper.INSTANCE
                .toPayload(storeService
                        .store(storeService
                                .save(file.getName(), auth.getName(), dirId)));
    }

    @Autowired
    public void setStoreService(StoreFileService storeService) {
        this.storeService = storeService;
    }
}
