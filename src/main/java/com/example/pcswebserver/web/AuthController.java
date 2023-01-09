package com.example.pcswebserver.web;


import com.example.pcswebserver.service.UserService;
import com.example.pcswebserver.web.dao.Credentials;
import com.example.pcswebserver.web.dao.JwtToken;
import com.example.pcswebserver.web.dao.JwtTokenMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
    public ResponseEntity<JwtToken> signIn(@RequestBody @Valid Credentials credentials) {
        var token = userService.signIn(credentials.getUsername(), credentials.getPassword());
        return ResponseEntity.ok(JwtTokenMapper.INSTANCE.toDto(token));
    }
}