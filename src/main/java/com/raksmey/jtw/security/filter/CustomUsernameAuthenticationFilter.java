package com.raksmey.jtw.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raksmey.jtw.security.exception.AuthenticationExceptionImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomUsernameAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(CustomUsernameAuthenticationFilter.class);


    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomUsernameAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;

        try {
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            username = requestBody.get("username");
            password = requestBody.get("password");

        } catch (IOException e) {
            throw new AuthenticationExceptionImpl("Error parsing authentication request body");
        }

        username = (username != null) ? username.trim() : "";
        password = (password != null) ? password : "";

        logger.info("Username is :{}", username);
        logger.info("Password is :{}", password);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("CustomUsernamePasswordAuthenticationFilter is processing request: " + request.getRequestId());
        super.doFilter(request, response, chain);
    }
}
