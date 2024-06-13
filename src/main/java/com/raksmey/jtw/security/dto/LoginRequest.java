package com.raksmey.jtw.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class LoginRequest {
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Please provide valid email address")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
