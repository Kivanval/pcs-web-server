package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "DIRS")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreDir {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    UUID id;
    @Column(nullable = false)
    String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    User creator;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    StoreDir parent;
    @Column(nullable = false)
    LocalDateTime createdAt = LocalDateTime.now();
    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "dir", cascade = CascadeType.ALL)
    Set<StoreFile> files = new HashSet<>();
    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<StoreDir> children = new HashSet<>();

    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    @OneToMany(mappedBy = "dir", cascade = CascadeType.ALL)
    Set<StoreDirPermission> dirPermissions = new HashSet<>();

    public Set<StoreDir> getAllChildren() {
        return children.isEmpty() ? children : children.stream()
                .flatMap(dir -> dir.getAllChildren().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreDir that = (StoreDir) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
