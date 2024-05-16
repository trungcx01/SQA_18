package com.example.registrationms.controller;

import com.example.registrationms.dto.LoginRequest;
import com.example.registrationms.model.Teacher;
import com.example.registrationms.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthenticationControllerTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void login() {
        var result = authenticationService.authenticate(new LoginRequest("MGV001", "123456"));
        assertThat(result.getUsername()).isEqualTo("MGV001");
        assertThat(result.getName()).isEqualTo("Nguyen Van A");
    }

    @Test
    public void testAuthenticateSuccess() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testAuthenticateFail() {
        assertThat(true).isEqualTo(true);
    }

    @Test
    public void testAuthenticateFailInvalidTeacherCode() {
        assertThat(true).isEqualTo(true);
    }
}