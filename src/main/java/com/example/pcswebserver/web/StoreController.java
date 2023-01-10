package com.example.pcswebserver.web;

import com.example.pcswebserver.service.StoreService;
import com.example.pcswebserver.web.payload.UploadedFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

import static com.example.pcswebserver.web.StoreController.STORE_PREFIX;

@Controller
@RequestMapping(STORE_PREFIX)
public class StoreController {

    public static final String STORE_PREFIX = "/store";

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/upload/{uuid}")
    public UploadedFile uploadFile(@RequestParam("file") MultipartFile file,
                                   @PathVariable String path) {
        String fileName = storeService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return null;
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadedFile> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return null;
    }

    @GetMapping("/file/{uuid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = storeService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
