package com.example.pcswebserver.security;

import com.example.pcswebserver.data.PermissionRepository;
import com.example.pcswebserver.data.UserRepository;
import com.example.pcswebserver.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        Set<SimpleGrantedAuthority> externalAuthorities = user.getPermissions()
                .stream()
                .map(Permission::asGrantedAuthority)
                .collect(Collectors.toSet());
        return new UserDetailsImpl(user, externalAuthorities);
    }
}