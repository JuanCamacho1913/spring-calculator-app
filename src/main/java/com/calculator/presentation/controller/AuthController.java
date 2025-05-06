package com.calculator.presentation.controller;

import com.calculator.presentation.dto.login.AuthLoginRequest;
import com.calculator.presentation.dto.register.AuthRegisterResponse;
import com.calculator.presentation.dto.register.AuthRegisterRequest;
import com.calculator.presentation.dto.login.AuthLoginResponse;
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
    public ResponseEntity<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest authLoginRequest){
        return new ResponseEntity<>(this.userDetailService.loginUser(authLoginRequest), HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRegisterResponse> register(@RequestBody @Valid AuthRegisterRequest authRegisterRequest){
        return new ResponseEntity<>(this.userDetailService.registerUser(authRegisterRequest), HttpStatus.CREATED);
    }
}
