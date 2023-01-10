package com.example.pcswebserver.web;


import com.example.pcswebserver.service.UserService;
import com.example.pcswebserver.web.payload.Credentials;
import com.example.pcswebserver.web.payload.JwtToken;
import com.example.pcswebserver.web.payload.JwtTokenMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import static com.example.pcswebserver.web.AuthController.AUTH_PREFIX;

@RestController
@RequestMapping(AUTH_PREFIX)
public class AuthController {

    public static final String AUTH_PREFIX = "/auth";

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@RequestBody @Valid Credentials credentials) {
        userService.signUp(credentials.getUsername(), credentials.getPassword());
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public JwtToken signIn(@RequestBody @Valid Credentials credentials) {
        return userService.signIn(credentials.getUsername(), credentials.getPassword())
                .map(JwtTokenMapper.INSTANCE::toPayload)
                .orElseThrow(() -> new AccessDeniedException("Access denied"));
    }
}