package com.semicolon.diary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;

@Data
public class SignUpRequest {

    private String firstName;
    private String lastName;
    @Email @NotBlank(message = "This Field Cannot be Blank")
    private String emailAddress;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$")
    private String password;
}
