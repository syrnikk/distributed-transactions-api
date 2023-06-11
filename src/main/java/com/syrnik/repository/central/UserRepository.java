package com.syrnik.repository.central;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.syrnik.model.central.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
