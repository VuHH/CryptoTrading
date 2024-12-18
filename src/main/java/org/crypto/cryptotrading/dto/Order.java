package org.crypto.cryptotrading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public class Order {

  private String orderId;

  @NotNull(message = "User ID cannot be null")
  private Long userId;

  @NotEmpty(message = "Symbol cannot be empty")
  @Pattern(regexp = "^(BTC|ETH)$", message = "Symbol must be either BTC or ETH")
  private String symbol;

  @NotEmpty(message = "Trading type cannot be empty")
  @Pattern(regexp = "^(BUY|SELL)$", message = "Type must be either BUY or SELL")
  private String typeTrading;

  @NotNull(message = "Amount cannot be null")
  @DecimalMin(value = "0.0001", message = "Amount must be greater than or equal to 0.0001")
  private BigDecimal amount;

  @NotNull(message = "Price cannot be null")
  @DecimalMin(value = "0.0001", message = "Amount must be greater than or equal to 0.0001")
  private BigDecimal priceCrypto;

  public Order() {}

  public Order(
      String orderId,
      Long userId,
      String symbol,
      String typeTrading,
      BigDecimal amount,
      BigDecimal priceCrypto) {
    this.orderId = orderId;
    this.userId = userId;
    this.symbol = symbol;
    this.typeTrading = typeTrading;
    this.amount = amount;
    this.priceCrypto = priceCrypto;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
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

  public BigDecimal getPriceCrypto() {
    return priceCrypto;
  }

  public void setPriceCrypto(BigDecimal priceCrypto) {
    this.priceCrypto = priceCrypto;
  }
}
