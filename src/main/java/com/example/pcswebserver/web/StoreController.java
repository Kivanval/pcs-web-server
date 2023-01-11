package com.example.pcswebserver.web;

import com.example.pcswebserver.service.StoreService;
import com.example.pcswebserver.web.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.example.pcswebserver.web.StoreController.STORE_PREFIX;

@RestController
@RequestMapping(STORE_PREFIX)
public class StoreController {

    public static final String STORE_PREFIX = "/store";
    private StoreService storeService;

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile upload(@RequestParam("file") MultipartFile file,
                               Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService
                        .store(file, storeService
                                .create(file, auth.getName())));
    }

    @PostMapping("/file/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile upload(@RequestParam("file") MultipartFile file,
                               @PathVariable("dir-id") UUID dirId,
                               Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService
                        .store(file, storeService
                                .create(file, auth.getName(), dirId)));
    }

    @PostMapping("/dir")
    @ResponseStatus(HttpStatus.OK)
    public CreatedDirectory create(@RequestBody CreateDirectory dir,
                                   Authentication auth) {
        return CreatedDirectoryMapper.INSTANCE
                .toPayload(storeService
                        .create(dir.getName(), auth.getName()));
    }

    @PostMapping("/dir/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public CreatedDirectory create(@RequestParam String name,
                                   @PathVariable("dir-id") UUID dirId,
                                   Authentication auth) {
        return CreatedDirectoryMapper.INSTANCE
                .toPayload(storeService
                        .create(name, auth.getName(), dirId));
    }


    @Autowired
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }
}
