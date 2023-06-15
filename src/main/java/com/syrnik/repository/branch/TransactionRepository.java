package com.syrnik.repository.branch;

import org.springframework.data.jpa.repository.JpaRepository;
import com.syrnik.model.branch.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
