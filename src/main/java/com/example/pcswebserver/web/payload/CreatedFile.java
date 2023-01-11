package com.example.pcswebserver.web.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CreatedFile {
    UUID id;
    String name;
    String creator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String dir;
    String createdAt;
}