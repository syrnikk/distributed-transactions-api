package com.syrnik.repository.central;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.syrnik.model.central.Branch;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Optional<Branch> findByName(String name);
}
