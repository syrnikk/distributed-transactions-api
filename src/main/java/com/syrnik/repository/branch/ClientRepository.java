package com.syrnik.repository.branch;

import org.springframework.data.jpa.repository.JpaRepository;
import com.syrnik.model.branch.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
