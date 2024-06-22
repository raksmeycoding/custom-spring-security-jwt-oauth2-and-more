package com.raksmey.jtw.security.model.requestDto;


import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SignUpRequestDto {
    private String email;
    private String username;
    private String password;
}
