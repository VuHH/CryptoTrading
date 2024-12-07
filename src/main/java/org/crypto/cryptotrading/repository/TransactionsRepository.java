package org.crypto.cryptotrading.repository;

import org.crypto.cryptotrading.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByUserId(Long userId);
}
