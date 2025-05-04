package com.calculator.presentation.controller;

import com.calculator.presentation.dto.AuthLoginRequest;
import com.calculator.presentation.dto.AuthResponse;
import com.calculator.service.impl.UserDetailServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserDetailServiceImpl userDetailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest authLoginRequest){
        return new ResponseEntity<>(this.userDetailService.loginUser(authLoginRequest), HttpStatus.CREATED);
    }
}
