package org.crypto.cryptotrading.dto;

import lombok.*;

public class HuobiPrice {
  private String symbol;
  private double open;
  private double high;
  private double low;
  private double close;
  private double amount;
  private double vol;
  private int count;
  private double bid;
  private double bidSize;
  private double ask;
  private double askSize;

  public HuobiPrice() {}

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getOpen() {
    return open;
  }

  public void setOpen(double open) {
    this.open = open;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public double getClose() {
    return close;
  }

  public void setClose(double close) {
    this.close = close;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getVol() {
    return vol;
  }

  public void setVol(double vol) {
    this.vol = vol;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public double getBid() {
    return bid;
  }

  public void setBid(double bid) {
    this.bid = bid;
  }

  public double getBidSize() {
    return bidSize;
  }

  public void setBidSize(double bidSize) {
    this.bidSize = bidSize;
  }

  public double getAsk() {
    return ask;
  }

  public void setAsk(double ask) {
    this.ask = ask;
  }

  public double getAskSize() {
    return askSize;
  }

  public void setAskSize(double askSize) {
    this.askSize = askSize;
  }
}
