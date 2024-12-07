package org.crypto.cryptotrading.dto;


public class BinancePrice {
  private String symbol;
  private double bidPrice;
  private String bidQty;
  private double askPrice;
  private String askQty;

  public BinancePrice() {}

  public BinancePrice(
      String symbol, double bidPrice, String bidQty, double askPrice, String askQty) {
    this.symbol = symbol;
    this.bidPrice = bidPrice;
    this.bidQty = bidQty;
    this.askPrice = askPrice;
    this.askQty = askQty;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(double bidPrice) {
    this.bidPrice = bidPrice;
  }

  public String getBidQty() {
    return bidQty;
  }

  public void setBidQty(String bidQty) {
    this.bidQty = bidQty;
  }

  public double getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(double askPrice) {
    this.askPrice = askPrice;
  }

  public String getAskQty() {
    return askQty;
  }

  public void setAskQty(String askQty) {
    this.askQty = askQty;
  }
}
