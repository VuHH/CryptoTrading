package org.crypto.cryptotrading.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Crypto")
public class Crypto implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Column(name = "crypto_symbol")
  private String cryptoSymbol;

  @Column(name = "bid_price")
  private BigDecimal bidPrice;

  @Column(name = "ask_price")
  private BigDecimal askPrice;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;

  public Crypto() {
  }

  public Crypto(
      Long id,
      String cryptoSymbol,
      BigDecimal bidPrice,
      BigDecimal askPrice,
      LocalDateTime lastUpdated) {
    this.id = id;
    this.cryptoSymbol = cryptoSymbol;
    this.bidPrice = bidPrice;
    this.askPrice = askPrice;
    this.lastUpdated = lastUpdated;
  }

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
