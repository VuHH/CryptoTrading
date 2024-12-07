package org.crypto.cryptotrading.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_crypto")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "wallet_balance_usdt")
  private BigDecimal walletBalanceUsdt;

  public User(Long id, String name, BigDecimal walletBalanceUsdt) {
    this.id = id;
    this.name = name;
    this.walletBalanceUsdt = walletBalanceUsdt;
  }

  public User() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getWalletBalanceUsdt() {
    return walletBalanceUsdt;
  }

  public void setWalletBalanceUsdt(BigDecimal walletBalanceUsdt) {
    this.walletBalanceUsdt = walletBalanceUsdt;
  }
}
