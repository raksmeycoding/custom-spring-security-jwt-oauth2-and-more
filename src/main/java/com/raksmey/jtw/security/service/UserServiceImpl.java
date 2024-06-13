package com.raksmey.jtw.security.service;

import com.raksmey.jtw.security.dto.*;
import com.raksmey.jtw.security.model.User;
import com.raksmey.jtw.security.repository.UserRepository;
import com.raksmey.jtw.security.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    private final TokenProvider tokenProvider;


    private final CookieUtil cookieUtil;

    public UserServiceImpl(UserRepository userRepository, TokenProvider tokenProvider, CookieUtil cookieUtil) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken) {
        String email = loginRequest.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found with email " + email));

//        boolean accessTokenValid = tokenProvider.validateToken(accessToken);
//        boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);

        HttpHeaders responseHeaders = new HttpHeaders();
//        Token newAccessToken;
//        Token newRefreshToken;
//        if (!accessTokenValid && !refreshTokenValid) {
//            newAccessToken = tokenProvider.generateAccessToken(user.getUsername());
//            newRefreshToken = tokenProvider.generateRefreshToken(user.getUsername());
//            addAccessTokenCookie(responseHeaders, newAccessToken);
//            addRefreshTokenCookie(responseHeaders, newRefreshToken);
//        }
//
//        if (!accessTokenValid && refreshTokenValid) {
//            newAccessToken = tokenProvider.generateAccessToken(user.getUsername());
//            addAccessTokenCookie(responseHeaders, newAccessToken);
//        }
//
//        if (accessTokenValid && refreshTokenValid) {
//            newAccessToken = tokenProvider.generateAccessToken(user.getUsername());
//            newRefreshToken = tokenProvider.generateRefreshToken(user.getUsername());
//            addAccessTokenCookie(responseHeaders, newAccessToken);
//            addRefreshTokenCookie(responseHeaders, newRefreshToken);
//        }

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);

    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken) {
        boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);
        if (!refreshTokenValid) {
            throw new IllegalArgumentException("Refresh Token is invalid!");
        }

        String currentUserEmail = tokenProvider.getUsernameFromToken(accessToken);

        Token newAccessToken = tokenProvider.generateAccessToken(currentUserEmail);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration()).toString());

        LoginResponse loginResponse = new LoginResponse(LoginResponse.SuccessFailure.SUCCESS, "Auth successful. Tokens are created in cookie.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    @Override
    public UserSummary getUserProfile() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findByEmail("david@gmail.com").orElseThrow(() -> new IllegalArgumentException("User not found with email "));
        return user.toUserSummary();
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }
}
