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
        var len = permissionType.toString().length();
        var stringBuilder = new StringBuilder(permissionType.toString() + "_" + file.getId());
        var dir = file.getDir();
        while (dir != null) {
            stringBuilder.insert(len, "_" + dir.getId());
            dir = dir.getParent();
        }
        return new SimpleGrantedAuthority(stringBuilder.toString());
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
