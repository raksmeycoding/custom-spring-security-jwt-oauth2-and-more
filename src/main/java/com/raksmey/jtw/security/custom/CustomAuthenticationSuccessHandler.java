package com.raksmey.jtw.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raksmey.jtw.security.exception.AuthenticationExceptionImpl;
import com.raksmey.jtw.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${jwt.cookieExpiry}")
    private int cookieExpiry;

    public CustomAuthenticationSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String accessToken = jwtService.GenerateToken(authentication.getName());

        ResponseCookie cookie = ResponseCookie.from("access-token", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        response.getWriter().write("Authentication successful!");
    }
}

