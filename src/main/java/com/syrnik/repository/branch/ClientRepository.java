package com.syrnik.repository.branch;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.syrnik.model.branch.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByUserId(Integer id);
}
