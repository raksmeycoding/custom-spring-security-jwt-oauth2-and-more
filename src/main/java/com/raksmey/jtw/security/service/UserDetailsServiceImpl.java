package com.raksmey.jtw.security.service;


import com.raksmey.jtw.security.dto.CustomUserDetails;
import com.raksmey.jtw.security.model.User;
import com.raksmey.jtw.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + s));
        logger.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
    }
}
