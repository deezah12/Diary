package com.semicolon.diary.dto;

import lombok.Data;

@Data
public class SignUpResponse {
    String firstName;
    String lastName;
    String emailAddress;
    String token;
}
