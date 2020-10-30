package hu.bme.aut.freelancer_spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    private Long id;

    private String name;

    private String email;

    private String phonenumber;

}
