package org.crypto.cryptotrading.repository;

import java.util.List;
import org.crypto.cryptotrading.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserIdAndCryptoSymbol(Long userId, String cryptoSymbol);
    List<Wallet> findByUserId(Long userId);
}
