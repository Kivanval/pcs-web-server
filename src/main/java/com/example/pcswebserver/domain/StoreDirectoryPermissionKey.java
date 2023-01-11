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
public class StoreDirectoryPermissionKey implements Serializable {
    @Column(name = "user_id")
    UUID userId;
    @Column(name = "dir_id")
    UUID dirId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreDirectoryPermissionKey that = (StoreDirectoryPermissionKey) o;
        return userId != null && Objects.equals(userId, that.userId)
                && dirId != null && Objects.equals(dirId, that.dirId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, dirId);
    }
}