package com.example.registrationms.controller;

import com.example.registrationms.dto.LoginRequest;
import com.example.registrationms.service.AuthenticationService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;


@SpringBootTest
class AuthenticationControllerTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void loginTrue() {
        var result = authenticationService.authenticate(new LoginRequest("MGV001", "12345"));
        assertEquals(result.getUsername(), "MGV001");
        assertEquals(result.getName(), "Nguyen Van A");
    }

    @Test()
    void loginNullUsername() {
        Throwable exception = assertThrows(AuthenticationException.class,
                () -> authenticationService.authenticate(new LoginRequest(null, "123456")));
        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    void loginNullPassword() {
        Throwable exception = assertThrows(AuthenticationException.class,
                () -> authenticationService.authenticate(new LoginRequest("MGV001", null)));
        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    void loginWrongPassword() {
        Throwable exception = assertThrows(AuthenticationException.class,
                () -> authenticationService.authenticate(new LoginRequest("MGV001", "123455")));
        assertEquals("Bad credentials", exception.getMessage());
    }
}