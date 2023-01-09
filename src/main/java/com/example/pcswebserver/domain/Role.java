package com.example.pcswebserver.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "role")
    @Setter(AccessLevel.PRIVATE)
    @ToString.Exclude
    Set<User> users = new HashSet<>();


    @ManyToOne
    Role parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<Role> children = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return id != null && Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addChild(Role role) {
        role.setParent(this);
        children.add(role);
    }

    public Set<Role> getAllChildren() {
        if (children.isEmpty()) return children;
        return children
                .stream()
                .flatMap(node -> node.getAllChildren().stream())
                .collect(Collectors.toSet());
    }

    public static final String ROLE_PREFIX = "ROLE_";

    public Set<SimpleGrantedAuthority> asAuthorities() {
        return Stream
                .concat(Stream.of(this), Stream.of(getAllChildren()))
                .map(node -> ROLE_PREFIX + name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public static final String ADMIN = "ADMIN";


    public static final String USER = "USER";

}