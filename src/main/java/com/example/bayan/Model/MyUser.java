package com.example.bayan.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
public class MyUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotEmpty(message = "username is required")
    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Column(nullable = false, length = 100,unique = true)
    @NotEmpty(message = "Email is required")
    @Size(min = 3, max = 100, message = "Email must be between 3 and 100 characters")
    private String email;

    @Column(nullable = false, length = 300)
    @NotEmpty(message = "Password is required")
    private String password;

    @Column(nullable = false, length = 50)
    @NotEmpty(message = "Role is required")
    @Size(min = 3, max = 50, message = "Role must be between 3 and 50 characters")
    @Pattern(regexp = "^CUSTOMER|BROKER|ADMIN|UNACTIVE$")
    //Customer or broker
    private String role;

    @Column(length = 50,unique = true)
    private String phoneNumber;

    @Column(length = 100)
    private String fullName;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relations
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private CustomsBroker broker;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
