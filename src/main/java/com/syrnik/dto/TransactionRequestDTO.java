package com.syrnik.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private BigDecimal amount;
    private String description;
}
