package com.example.pcswebserver.web.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadedFile {

    UUID id;

    String name;

    String creator;

    String dir;

    String contentType;

    Long size;

    String createdAt;
}
