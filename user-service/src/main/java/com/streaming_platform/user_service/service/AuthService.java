package com.streaming_platform.user_service.service;

import com.streaming_platform.user_service.dto.AuthResponse;
import com.streaming_platform.user_service.dto.LoginRequest;
import com.streaming_platform.user_service.dto.SignupRequest;
import com.streaming_platform.user_service.model.User;
import com.streaming_platform.user_service.repository.UserRepository;
import com.streaming_platform.user_service.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthResponse signup(SignupRequest request){
        String decodedPass = encoder.encode(request.getPassword());
        User user = User.builder().email(request.getEmail()).password(decodedPass).fullname(request.getFullname()).build();
        String accessToken = jwtUtil.generateAccessToken(user.getId().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());
        user.setRefreshToken(refreshToken);
        repository.save(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request){
        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        if (!encoder.matches(request.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Email/Password");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());
        user.setRefreshToken(refreshToken);
        repository.save(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken){
        Claims claims = jwtUtil.getClaims(refreshToken);
        String userId = claims.getSubject();

        User u = repository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER not found"));

        if (!refreshToken.equals(u.getRefreshToken())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Refresh Token");
        }
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        return new AuthResponse(newAccessToken, refreshToken);
    }
}
