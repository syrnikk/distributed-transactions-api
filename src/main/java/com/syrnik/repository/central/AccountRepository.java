package com.syrnik.repository.central;

import org.springframework.data.jpa.repository.JpaRepository;
import com.syrnik.model.central.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
