package com.raksmey.jtw.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "authority")
public class Authority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq")
    @SequenceGenerator(name = "authority_seq", sequenceName = "authority_sequence", allocationSize = 1)
    private Long authorityId;

    @Column(length = 100, unique = true)
    @NotNull
    private String authorityName;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User> users;

    public GrantedAuthority grantedAuthority() {
        return new SimpleGrantedAuthority(this.authorityName);
    }
}
