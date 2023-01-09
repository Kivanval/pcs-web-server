package com.example.pcswebserver.data;

import com.example.pcswebserver.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.imageio.ImageTranscoder;
import java.util.List;


@Service
public class RoleLoader implements CommandLineRunner {

    RoleRepository roleRepository;

    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public void run(String... args) {
        var admin = new Role();
        admin.setName(Role.ADMIN);
        admin = roleRepository.save(admin);
        var user = new Role();
        user.setName(Role.USER);
        admin.addChild(user);
        roleRepository.save(user);
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
