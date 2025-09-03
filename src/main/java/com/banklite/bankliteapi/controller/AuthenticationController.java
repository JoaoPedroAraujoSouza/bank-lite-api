package com.banklite.bankliteapi.controller;

import com.banklite.bankliteapi.dto.auth.LoginRequest;
import com.banklite.bankliteapi.dto.auth.LoginResponse;
import com.banklite.bankliteapi.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }
}
