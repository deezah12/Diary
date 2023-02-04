package com.semicolon.diary.dto;

import lombok.Data;

@Data
public class SignUpRequest {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
}
