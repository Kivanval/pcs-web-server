package com.example.pcswebserver.data;


import com.example.pcswebserver.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    User getByUsername(String username);

    boolean existsByUsername(String username);
}