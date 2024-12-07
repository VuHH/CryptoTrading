package org.crypto.cryptotrading.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinancePrice {
    private String symbol;
    private String bidPrice;
    private String bidQty;
    private String askPrice;
    private String askQty;
}
