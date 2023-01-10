package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "DIR_PERMS")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreDirectoryPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    UUID id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(optional = false)
    @MapsId("directoryId")
    @JoinColumn(name = "directory_id")
    StoreDirectory directory;

    @Enumerated(EnumType.STRING)
    StorePermissionType permissionType;

    @Column(nullable = false)
    LocalDateTime grantedAt = LocalDateTime.now();

    public Set<SimpleGrantedAuthority> asAuthorities() {
        return Stream
                .concat(Stream.of(directory), Stream.of(directory.getAllChildren()))
                .map(dir -> permissionType.toString() + "_" + directory.getId())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreDirectoryPermission that = (StoreDirectoryPermission) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
