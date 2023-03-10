package com.example.pcswebserver.web;

import com.example.pcswebserver.service.StoreFileService;
import com.example.pcswebserver.service.impl.StoreFileServiceImpl;
import com.example.pcswebserver.web.payload.CreateFile;
import com.example.pcswebserver.web.payload.CreatedFile;
import com.example.pcswebserver.web.payload.UploadedFile;
import com.example.pcswebserver.web.payload.mapper.CreatedFileMapper;
import com.example.pcswebserver.web.payload.mapper.UploadedFileMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.example.pcswebserver.web.WebConstants.*;

@RestController
@RequestMapping(STORE + FILE)
@Slf4j
@SecurityRequirement(name = "pcs-api")
public class StoreFileController {
    private StoreFileService storeService;

    @GetMapping(DOWNLOAD + "/{file-id}")
    public ResponseEntity<Resource> download(@PathVariable("file-id") UUID id) {
        var file = storeService.getById(id);
        var resource = storeService.load(id);

        String contentType = null;
        try {
            contentType = Files.probeContentType(Path.of(file.getName()));
        } catch (IOException ex) {
            log.info("Could not determine file type by id {}", id);
        }
        if (contentType == null)
            contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @PostMapping(UPLOAD)
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile upload(@RequestParam("file") MultipartFile file,
                               Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService
                        .upload(file, auth.getName()));
    }

    @PostMapping(UPLOAD + "/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile upload(@RequestParam("file") MultipartFile file,
                               @PathVariable("dir-id") UUID dirId,
                               Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService
                        .upload(file, auth.getName(), dirId));
    }

    @PostMapping(CREATE)
    @ResponseStatus(HttpStatus.OK)
    public CreatedFile create(@RequestBody @Valid CreateFile file,
                              Authentication auth) {
        return CreatedFileMapper.INSTANCE
                .toPayload(storeService
                        .create(file.getName(), auth.getName()));
    }

    @PostMapping(CREATE + "/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatedFile create(@RequestBody @Valid CreateFile file,
                              @PathVariable("dir-id") UUID dirId,
                              Authentication auth) {
        return CreatedFileMapper.INSTANCE
                .toPayload(storeService
                        .create(file.getName(), auth.getName(), dirId));
    }

    @DeleteMapping(DELETE + "/{file-id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("file-id") UUID id) {
        storeService.delete(id);
    }

    @Autowired
    public void setStoreService(StoreFileServiceImpl storeService) {
        this.storeService = storeService;
    }
}
