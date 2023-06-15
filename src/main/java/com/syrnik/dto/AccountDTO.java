package com.syrnik.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
    private String accountNumber;
    private BigDecimal balance;
}
