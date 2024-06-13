package com.raksmey.jtw.security.controller;


import com.raksmey.jtw.security.dto.UserSummary;
import com.raksmey.jtw.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/test")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserSummary> me() {
        return ResponseEntity.ok(userService.getUserProfile());
    }
}
