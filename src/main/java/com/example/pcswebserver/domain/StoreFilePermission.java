package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "FILE_PERMS")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreFilePermission {
    @Id
    @Column(nullable = false)
    UUID id;
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
        return new SimpleGrantedAuthority(permissionType.toString() + "_" + file.getId());
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
