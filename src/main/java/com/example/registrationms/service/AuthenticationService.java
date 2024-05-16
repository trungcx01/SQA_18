package com.example.registrationms.service;

import com.example.registrationms.dto.AuthenticationResponse;
import com.example.registrationms.dto.LoginRequest;
import com.example.registrationms.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepo.findByUsername(request.getUsername()).orElseThrow();
        var jwt = jwtService.generateJwt(user);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}
