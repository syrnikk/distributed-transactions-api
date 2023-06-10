package com.syrnik.model.branch;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    @Column(name = "recipient_account_number", nullable = false)
    private String recipientAccountNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
}
