package com.streaming_platform.user_service.controller;

import com.streaming_platform.user_service.dto.AuthResponse;
import com.streaming_platform.user_service.dto.LoginRequest;
import com.streaming_platform.user_service.dto.SignupRequest;
import com.streaming_platform.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest req){
        return ResponseEntity.ok(service.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(service.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body){
        String refreshToken = body.get("refreshToken");
        return ResponseEntity.ok(service.refresh(refreshToken));
    }

}
