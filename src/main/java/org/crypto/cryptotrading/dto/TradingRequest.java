package org.crypto.cryptotrading.dto;

import java.math.BigDecimal;

public class TradingRequest {
  private Long userId;
  private String symbol;
  private String typeTrading;
  private BigDecimal amount;

  public TradingRequest(Long userId, String symbol, String typeTrading, BigDecimal amount) {
    this.userId = userId;
    this.symbol = symbol;
    this.typeTrading = typeTrading;
    this.amount = amount;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getTypeTrading() {
    return typeTrading;
  }

  public void setTypeTrading(String typeTrading) {
    this.typeTrading = typeTrading;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
