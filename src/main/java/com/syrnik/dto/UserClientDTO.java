package com.syrnik.dto;

import java.time.LocalDate;

import com.syrnik.enums.Role;
import com.syrnik.model.central.Branch;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserClientDTO {
    private int id;
    private String login;
    private Role role;
    private String branchName;
    private String firstName;
    private String secondName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String gender;
}
