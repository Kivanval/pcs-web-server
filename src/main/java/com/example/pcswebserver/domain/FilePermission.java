package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "FILE_PERMISSIONS")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilePermission {

    @EmbeddedId
    FilePermissionKey id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(optional = false)
    @MapsId("fileSystemNodeId")
    @JoinColumn(name = "file_system_node_id")
    FileSystemNode fileSystemNode;

    @ManyToOne(optional = false)
    @MapsId("filePermissionTypeId")
    @JoinColumn(name = "file_permission_type_id")
    FilePermissionType filePermissionType;

    public Set<SimpleGrantedAuthority> asAuthorities() {
        return Stream
                .concat(Stream.of(fileSystemNode), Stream.of(fileSystemNode.getAllChildren()))
                .map(node -> "%s_%s" .formatted(filePermissionType.getName(), fileSystemNode.getFullName()))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FilePermission that = (FilePermission) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
