package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "FILE_SYSTEM_NODES")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileSystemNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @Column(nullable = false, unique = true)
    String name;


    @ManyToOne(optional = false)
    FileSystemNode parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<FileSystemNode> children = new HashSet<>();

    public Set<FileSystemNode> getAllChildren() {
        if (children.isEmpty()) return children;
        return children
                .stream()
                .flatMap(node -> node.getAllChildren().stream())
                .collect(Collectors.toSet());
    }

    public String getFullName() {
       if(parent == null) return name;
       return "%s/%s".formatted(parent.getFullName(), name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FileSystemNode that = (FileSystemNode) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
