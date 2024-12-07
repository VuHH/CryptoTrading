package org.crypto.cryptotrading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transactions {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "crypto_symbol", nullable = false)
  private String cryptoSymbol;

  @Column(name = "transaction_type", nullable = false)
  private String transactionType;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private LocalDateTime timestamp;
}
