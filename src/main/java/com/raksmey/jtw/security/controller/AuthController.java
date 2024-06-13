package com.raksmey.jtw.security.controller;


import com.raksmey.jtw.security.dto.LoginResponse;
import com.raksmey.jtw.security.model.dtos.AuthRequestDTO;
import com.raksmey.jtw.security.model.dtos.JwtResponseDTO;
import com.raksmey.jtw.security.model.RefreshToken;
import com.raksmey.jtw.security.service.JwtService;
import com.raksmey.jtw.security.service.RefreshTokenService;
import com.raksmey.jtw.security.service.UserService;
import com.raksmey.jtw.security.util.SecurityCipher;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;


    @Value("${jwt.cookieExpiry}")
    private int cookieExpiry;


//    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<LoginResponse> login(
//            @CookieValue(name = "accessToken", required = false) String accessToken,
//            @CookieValue(name = "refreshToken", required = false) String refreshToken,
//            @Valid @RequestBody LoginRequest loginRequest
//    ) {
//       try {
//           Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//           SecurityContextHolder.getContext().setAuthentication(authentication);
//       }catch (Exception e) {
//           e.printStackTrace();
//       }
//
////        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
////        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
//        return userService.login(loginRequest, null, null);
//    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = "accessToken", required = false) String accessToken,
                                                      @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return userService.refresh(decryptedAccessToken, decryptedRefreshToken);
    }


    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            String accessToken = jwtService.GenerateToken(authRequestDTO.getUsername());
            // set accessToken to cookie header
            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token(refreshToken.getToken()).build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }
}
