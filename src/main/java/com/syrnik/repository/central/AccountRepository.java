package com.syrnik.repository.central;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.syrnik.model.central.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    List<Account> findAllByUserId(@Param("userId") int userId);

    Optional<Account> findByAccountNumber(String loggedInUserAccountNumber);
}
