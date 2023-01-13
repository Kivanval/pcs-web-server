package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "FILE_PERMS")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreFilePermission {
    @EmbeddedId
    StoreFilePermissionKey id = new StoreFilePermissionKey();
    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne(optional = false)
    @MapsId("fileId")
    @JoinColumn(name = "file_id")
    StoreFile file;
    @Enumerated(EnumType.STRING)
    StorePermissionType permissionType;
    @Column(nullable = false)
    LocalDateTime grantedAt = LocalDateTime.now();

    public SimpleGrantedAuthority asAuthority() {
        var dirId = file.getDir() != null ? file.getDir().getId().toString() : "";
        var fileId = file.getId().toString();
        return new SimpleGrantedAuthority(String.join("_", permissionType.toString(), dirId, fileId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreFilePermission that = (StoreFilePermission) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
