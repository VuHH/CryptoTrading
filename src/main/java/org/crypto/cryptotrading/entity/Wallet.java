package org.crypto.cryptotrading.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
public class Wallet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "crypto_symbol", nullable = false)
  private String cryptoSymbol;

  @Column(name = "balance", nullable = false)
  private BigDecimal balance;

  public Wallet(Long id, Long userId, String cryptoSymbol, BigDecimal balance) {
    this.id = id;
    this.userId = userId;
    this.cryptoSymbol = cryptoSymbol;
    this.balance = balance;
  }

  public Wallet() {}

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

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }
}
