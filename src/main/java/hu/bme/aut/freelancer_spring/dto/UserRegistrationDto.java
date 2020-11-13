package hu.bme.aut.freelancer_spring.dto;

import hu.bme.aut.freelancer_spring.validators.PasswordMatches;
import hu.bme.aut.freelancer_spring.validators.ValidEmail;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@PasswordMatches
@ToString
public class UserRegistrationDto {

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String passwordConfirm;

    @NotNull
    @NotEmpty
    private String phonenumber;

    private boolean hasInsurance;
}
