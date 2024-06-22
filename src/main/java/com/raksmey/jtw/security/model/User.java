package com.raksmey.jtw.security.model;

import com.raksmey.jtw.security.dto.UserSummary;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    private Long userId;

    @Column(length = 50, unique = true)
    @NotNull
    private String username;

    @Column(length = 100, unique = true)
    @NotNull
    @Email
    private String email;

    @Column(length = 200)
    private String password;

    @NotNull
    private Boolean enabled;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "authorityId", referencedColumnName = "authorityId")})
    private Set<Authority> authorities;

    public UserSummary toUserSummary() {
        UserSummary userSummary = new UserSummary();
        userSummary.setEmail(this.email);
        userSummary.setUserId(this.userId);
        return userSummary;
    }
}
