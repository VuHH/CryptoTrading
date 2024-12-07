package org.crypto.cryptotrading.repository;

import org.crypto.cryptotrading.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserIdAndCryptoSymbol(Long userId, String cryptoSymbol);
    List<Wallet> findByUserId(Long userId);
}
