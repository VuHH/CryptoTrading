package org.crypto.cryptotrading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Crypto")
public class Crypto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "crypto_symbol")
  private String cryptoSymbol;

  @Column(name = "bid_price")
  private BigDecimal bidPrice;

  @Column(name = "ask_price")
  private BigDecimal askPrice;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;
}
