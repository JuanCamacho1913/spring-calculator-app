package com.calculator.presentation.controller;

import com.calculator.presentation.dto.login.AuthLoginRequest;
import com.calculator.presentation.dto.login.AuthLoginResponse;
import com.calculator.presentation.dto.register.AuthRegisterRequest;
import com.calculator.presentation.dto.register.AuthRegisterResponse;
import com.calculator.service.impl.UserDetailServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserDetailServiceImpl userDetailService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Should log in")
    void loginTest(){
        String username = "juan";
        String password = "1234";
        String message = "User logged in correctly";
        String token = "jwt-token";
        Boolean status = true;

        AuthLoginRequest authLoginRequest = new AuthLoginRequest(
                username,
                password
        );

        AuthLoginResponse authLoginResponse = new AuthLoginResponse(
                username,
                message,
                token,
                status
        );

        when(userDetailService.loginUser(authLoginRequest)).thenReturn(authLoginResponse);

        ResponseEntity<AuthLoginResponse> responseEntity = authController.login(authLoginRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(authLoginResponse, responseEntity.getBody());
        verify(userDetailService).loginUser(authLoginRequest);
    }

    @Test
    @DisplayName("Should register")
    void registerTest(){
        String username = "juan";
        String password = "1234";
        String email = "juan@gmail.com";
        String message = "User created successfully";
        Boolean status = true;

        AuthRegisterRequest request = new AuthRegisterRequest(
                username,
                password,
                email
        );

        AuthRegisterResponse response = new AuthRegisterResponse(
                username,
                email,
                message,
                status
        );

        when(userDetailService.registerUser(request)).thenReturn(response);

        ResponseEntity<AuthRegisterResponse> responseEntity = authController.register(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(userDetailService).registerUser(request);
    }
}
