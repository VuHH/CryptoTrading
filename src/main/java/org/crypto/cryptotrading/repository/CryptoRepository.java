package org.crypto.cryptotrading.repository;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.crypto.cryptotrading.entity.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {

  @Transactional
  @Modifying
  @Query(
      "UPDATE Crypto c SET c.bidPrice = :bidPrice, c.askPrice = :askPrice, c.lastUpdated = CURRENT_TIMESTAMP WHERE c.cryptoSymbol = :cryptoSymbol")
  int updateCryptoPrices(String cryptoSymbol, BigDecimal bidPrice, BigDecimal askPrice);

  Crypto findByCryptoSymbol(String cryptoSymbol);
}
