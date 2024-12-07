package org.crypto.cryptotrading.repository;

import java.util.List;
import org.crypto.cryptotrading.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByUserId(Long userId);
}
