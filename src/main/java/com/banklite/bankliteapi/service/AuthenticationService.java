package com.banklite.bankliteapi.service;

import com.banklite.bankliteapi.dto.auth.LoginRequest;
import com.banklite.bankliteapi.dto.auth.LoginResponse;
import com.banklite.bankliteapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    UserRepository userRepository;
    JwtService jwtService;
    AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate (
                new UsernamePasswordAuthenticationToken (
                        request.getLogin(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }
}
