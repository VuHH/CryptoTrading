package org.crypto.cryptotrading.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

  public Transactions() {}

  public Transactions(
      Long id,
      Long userId,
      String cryptoSymbol,
      String transactionType,
      BigDecimal amount,
      BigDecimal price,
      LocalDateTime timestamp) {
    this.id = id;
    this.userId = userId;
    this.cryptoSymbol = cryptoSymbol;
    this.transactionType = transactionType;
    this.amount = amount;
    this.price = price;
    this.timestamp = timestamp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getCryptoSymbol() {
    return cryptoSymbol;
  }

  public void setCryptoSymbol(String cryptoSymbol) {
    this.cryptoSymbol = cryptoSymbol;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
