package com.example.pcswebserver.web.payload;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatedPassword implements Serializable {
    @NotNull @Size(min = 5, max = 255) @Pattern(regexp = "^\\S+$") String username;
    @NotNull @Size(min = 8, max = 255) @Pattern(regexp = "^\\S+$") String oldPassword;

    @NotNull @Size(min = 8, max = 255) @Pattern(regexp = "^\\S+$") String newPassword;
}
