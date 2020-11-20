package hu.bme.aut.freelancer_spring.security;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

public class CustomUser extends User implements CustomUserDetails {

    private final Long id;

    public CustomUser(Long id, String email, String password) {
        super(email, password, new ArrayList<>());
        this.id = id;
    }

    @Override
    public Long getUserId() {
        return id;
    }

    @Override
    public String getEmail() {
        return super.getUsername();
    }
}