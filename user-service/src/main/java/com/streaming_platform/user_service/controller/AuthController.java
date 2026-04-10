package com.streaming_platform.user_service.controller;

import com.streaming_platform.user_service.dto.AuthResponse;
import com.streaming_platform.user_service.dto.LoginRequest;
import com.streaming_platform.user_service.dto.SignupRequest;
import com.streaming_platform.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest req){
        service.signup(req);
        return ResponseEntity.ok("User Created Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req){
        String token = service.login(req);
        return ResponseEntity.ok(new AuthResponse(token));
    }


}
