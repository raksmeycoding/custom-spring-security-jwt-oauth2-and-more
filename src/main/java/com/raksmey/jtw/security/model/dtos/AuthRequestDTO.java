package com.raksmey.jtw.security.model.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDTO {

    private String username;
    private String password;
}
