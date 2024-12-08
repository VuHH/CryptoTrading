package org.crypto.cryptotrading.repository;

import org.crypto.cryptotrading.entity.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {

  Crypto findByCryptoSymbol(String cryptoSymbol);
}
