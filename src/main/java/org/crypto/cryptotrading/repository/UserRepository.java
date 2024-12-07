package org.crypto.cryptotrading.repository;

import org.crypto.cryptotrading.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(Long id);
}
