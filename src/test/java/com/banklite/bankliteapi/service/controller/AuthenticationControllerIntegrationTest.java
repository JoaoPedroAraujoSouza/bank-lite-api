package com.banklite.bankliteapi.service.controller;

import com.banklite.bankliteapi.dto.auth.LoginRequest;
import com.banklite.bankliteapi.dto.auth.LoginResponse;
import com.banklite.bankliteapi.service.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void whenLoginWithValidAdminCredentials_shouldReturnTokens() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("admin");
        loginRequest.setPassword("admin123");

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/login",
                loginRequest,
                LoginResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAccessToken());
        assertFalse(response.getBody().getAccessToken().isBlank());
        assertNotNull(response.getBody().getRefreshToken());
        assertFalse(response.getBody().getRefreshToken().isBlank());
    }
}
