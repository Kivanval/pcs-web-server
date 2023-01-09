package com.example.pcswebserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilePermissionKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "file_system_node_id")
    Long fileSystemNodeId;

    @Column(name = "file_permission_type_id")
    Long filePermissionTypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FilePermissionKey that = (FilePermissionKey) o;
        return userId != null && Objects.equals(userId, that.userId)
                && fileSystemNodeId != null && Objects.equals(fileSystemNodeId, that.fileSystemNodeId)
                && filePermissionTypeId != null && Objects.equals(filePermissionTypeId, that.filePermissionTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, fileSystemNodeId, filePermissionTypeId);
    }
}
