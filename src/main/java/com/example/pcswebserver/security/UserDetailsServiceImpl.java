package com.example.pcswebserver.security;

import com.example.pcswebserver.data.StoreDirectoryPermissionRepository;
import com.example.pcswebserver.data.StoreFilePermissionRepository;
import com.example.pcswebserver.data.UserRepository;
import com.example.pcswebserver.domain.StoreFilePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final StoreFilePermissionRepository storeFilePermissionRepository;

    private final StoreDirectoryPermissionRepository storeDirectoryPermissionRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  StoreFilePermissionRepository storeFilePermissionRepository,
                                  StoreDirectoryPermissionRepository storeDirectoryPermissionRepository) {
        this.userRepository = userRepository;
        this.storeFilePermissionRepository = storeFilePermissionRepository;
        this.storeDirectoryPermissionRepository = storeDirectoryPermissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        var permissions = Stream.concat(
                storeFilePermissionRepository.findAllByUser(user)
                        .stream()
                        .map(StoreFilePermission::asAuthority),
                storeDirectoryPermissionRepository.findAllByUser(user)
                        .stream()
                        .flatMap(dir -> dir.asAuthorities().stream())
        ).collect(Collectors.toSet());
        permissions.addAll(user.getRole().asAuthorities());
        return new UserDetailsImpl(user, permissions);
    }
}