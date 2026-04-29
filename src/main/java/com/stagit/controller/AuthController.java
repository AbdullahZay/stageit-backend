package com.stagit.controller;

import com.stagit.dto.LoginRequest;
import com.stagit.dto.LoginResponse;
import com.stagit.dto.RegisterRequest;
import com.stagit.dto.UserResponse;
import com.stagit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request){
        UserResponse user = userService.register(request);
        return ResponseEntity.status(201).body(user);
    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse user = userService.login(request);
        return ResponseEntity.status(200).body(user);
    }

}
