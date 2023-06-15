package com.syrnik.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchDTO {
    private Integer id;
    private String name;
    private String address;
}
