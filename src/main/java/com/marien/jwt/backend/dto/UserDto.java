package com.marien.jwt.backend.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UserDto{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}


