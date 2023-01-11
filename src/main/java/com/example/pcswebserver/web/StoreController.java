package com.example.pcswebserver.web;

import com.example.pcswebserver.service.StoreService;
import com.example.pcswebserver.web.payload.UploadedFile;
import com.example.pcswebserver.web.payload.UploadedFileMapper;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.pcswebserver.web.StoreController.STORE_PREFIX;

@RestController
@RequestMapping(STORE_PREFIX)
public class StoreController {

    public static final String STORE_PREFIX = "/store";
    private StoreService storeService;

    @PostMapping("/file/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable("dir-id") @UUID String dirId,
            Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService.uploadFile(file, auth.getName(), dirId));
    }

    @PostMapping("/dir/{dir-id}")
    @ResponseStatus(HttpStatus.OK)
    public UploadedFile createFolder(
            @RequestParam("file") MultipartFile file,
            @PathVariable("dir-id") @UUID String dirId,
            Authentication auth) {
        return UploadedFileMapper.INSTANCE
                .toPayload(storeService.uploadFile(file, auth.getName(), dirId));
    }


    @Autowired
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }
}
