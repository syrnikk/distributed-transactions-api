package com.syrnik.repository.central;

import org.springframework.data.jpa.repository.JpaRepository;
import com.syrnik.model.central.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
