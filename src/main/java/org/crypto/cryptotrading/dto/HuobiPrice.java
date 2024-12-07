package org.crypto.cryptotrading.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
