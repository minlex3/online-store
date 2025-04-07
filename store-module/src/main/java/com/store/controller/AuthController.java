package com.store.controller;

import com.store.entity.UserData;
import com.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody UserData user) {
        return userService.registerUser(user)
                .thenReturn(ResponseEntity.ok("User registered successfully"));
    }

    @GetMapping("/login")
    public Mono<String> loginPage() {
        return Mono.just("login");
    }
}
