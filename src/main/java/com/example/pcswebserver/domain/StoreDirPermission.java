package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "DIR_PERMS")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreDirPermission {
    @EmbeddedId
    StoreDirPermissionKey id = new StoreDirPermissionKey();

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne(optional = false)
    @MapsId("dirId")
    @JoinColumn(name = "dir_id")
    StoreDir dir;
    @Enumerated(EnumType.STRING)
    StorePermissionType permissionType;
    @Column(nullable = false)
    LocalDateTime grantedAt = LocalDateTime.now();

    public Set<SimpleGrantedAuthority> asAuthorities() {
        return Stream
                .concat(Stream.of(dir), dir.getAllChildren().stream())
                .map(storeDir -> String.join("_", permissionType.toString(), storeDir.getId().toString()))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreDirPermission that = (StoreDirPermission) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
