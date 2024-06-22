package com.raksmey.jtw.security.service;


import com.raksmey.jtw.security.dto.LoginRequest;
import com.raksmey.jtw.security.dto.LoginResponse;
import com.raksmey.jtw.security.dto.UserSummary;
import com.raksmey.jtw.security.model.requestDto.SignUpRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken);

    UserSummary getUserProfile();

    void registerUser(SignUpRequestDto signUpRequestDto);
}
