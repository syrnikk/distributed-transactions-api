package com.syrnik.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RegisterDTO {
    private String login;
    private String password;
    private String branchName;
    private String firstName;
    private String secondName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String gender;
}