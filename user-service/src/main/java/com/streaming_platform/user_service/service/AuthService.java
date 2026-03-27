package com.streaming_platform.user_service.service;

import com.streaming_platform.user_service.dto.LoginRequest;
import com.streaming_platform.user_service.dto.SignupRequest;
import com.streaming_platform.user_service.model.User;
import com.streaming_platform.user_service.repository.UserRepository;
import com.streaming_platform.user_service.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public void signup(SignupRequest request){
        String decodedPass = encoder.encode(request.getPassword());
        User user = User.builder().email(request.getEmail()).password(decodedPass).build();
        repository.save(user);
    }

    public String login(LoginRequest request){
        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        if (!encoder.matches(request.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Email/Password");
        }
        return jwtUtil.generateToken(user.getId().toString());
    }
}
