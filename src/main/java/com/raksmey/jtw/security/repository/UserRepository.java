package com.raksmey.jtw.security.repository;


import com.raksmey.jtw.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    User findByUsername(String username);
    User findFirstByUserId(Long userId);
}
