package com.example.pcswebserver.web;

import com.example.pcswebserver.service.StoreDirService;
import com.example.pcswebserver.web.payload.CreateDir;
import com.example.pcswebserver.web.payload.CreatedDir;
import com.example.pcswebserver.web.payload.CreatedDirMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.pcswebserver.web.WebConstants.*;

@RestController
@RequestMapping(STORE + DIR)
public class StoreDirController {
    private StoreDirService storeService;

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
    public CreatedDir create(@RequestParam String name,
                             @PathVariable("dir-id") UUID dirId,
                             Authentication auth) {
        return CreatedDirMapper.INSTANCE
                .toPayload(storeService
                        .create(name, auth.getName(), dirId));
    }

    @Autowired
    public void setStoreService(StoreDirService storeService) {
        this.storeService = storeService;
    }
}