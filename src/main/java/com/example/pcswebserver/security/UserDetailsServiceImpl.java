package com.example.pcswebserver.security;

import com.example.pcswebserver.data.PermissionRepository;
import com.example.pcswebserver.data.UserRepository;
import com.example.pcswebserver.domain.FilePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    private PermissionRepository permissionRepository;

    @Autowired
    public void setUserRepository(UserRepository userService, PermissionRepository permissionRepository) {
        this.userRepository = userService;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        var authorities = permissionRepository.findByUser(user)
                .map(FilePermission::asAuthorities)
                .orElse(Set.of());
        authorities.addAll(user.getRole().asAuthorities());
        return new UserDetailsImpl(user, authorities);
    }
}