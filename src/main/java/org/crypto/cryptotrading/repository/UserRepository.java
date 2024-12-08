package org.crypto.cryptotrading.repository;

import java.util.Optional;
import org.crypto.cryptotrading.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findById(Long id);
}
