package hu.bme.aut.freelancer_spring.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    Long getUserId();

    String getEmail();
}