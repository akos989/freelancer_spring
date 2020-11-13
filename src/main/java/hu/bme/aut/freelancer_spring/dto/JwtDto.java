package hu.bme.aut.freelancer_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDto {
    private final String token;
    private final int expiresIn;
}
