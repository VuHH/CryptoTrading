package org.crypto.cryptotrading.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCryptoSymbol() {
    return cryptoSymbol;
  }

  public void setCryptoSymbol(String cryptoSymbol) {
    this.cryptoSymbol = cryptoSymbol;
  }

  public BigDecimal getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(BigDecimal bidPrice) {
    this.bidPrice = bidPrice;
  }

  public BigDecimal getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(BigDecimal askPrice) {
    this.askPrice = askPrice;
  }

  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(LocalDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
