package com.example.pcswebserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreFilePermissionKey implements Serializable {

    @Column(name = "user_id")
    UUID userId;

    @Column(name = "file_id")
    UUID fileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreFilePermissionKey that = (StoreFilePermissionKey) o;
        return userId != null && Objects.equals(userId, that.userId)
                && fileId != null && Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, fileId);
    }
}
