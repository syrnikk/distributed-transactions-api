package com.syrnik.repository.branch;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.syrnik.model.branch.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("select t from Transaction t where t.senderAccountNumber in :accountNumbers or t.recipientAccountNumber " +
           "in :accountNumbers")
    List<Transaction> findUserTransactions(@Param("accountNumbers") List<String> accountNumbers);
}
