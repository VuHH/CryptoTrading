package org.crypto.cryptotrading.dto.mapper;

import org.crypto.cryptotrading.dto.BinancePrice;
import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.entity.Crypto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CryptoMapper {
  public List<Crypto> mapPricesToCrypto(
      List<BinancePrice> binancePrices, List<HuobiPrice> huobiPrices) {
    Map<String, HuobiPrice> huobiPriceMap =
        huobiPrices.stream()
            .collect(Collectors.toMap(HuobiPrice::getSymbol, huobiPrice -> huobiPrice));

    return binancePrices.stream()
        .filter(
            binancePrice ->
                "BTCUSDT".equals(binancePrice.getSymbol())
                    || "ETHUSDT".equals(binancePrice.getSymbol()))
        .map(
            binancePrice -> {
              HuobiPrice huobiPrice = huobiPriceMap.get(binancePrice.getSymbol().toLowerCase());
              Crypto crypto = new Crypto();
              crypto.setCryptoSymbol(extractSymbol(binancePrice.getSymbol()));
              crypto.setBidPrice(
                  binancePrice.getBidPrice() != 0
                      ? BigDecimal.valueOf(binancePrice.getBidPrice())
                      : BigDecimal.valueOf(huobiPrice.getBid()));
              crypto.setAskPrice(
                  binancePrice.getAskPrice() != 0
                      ? BigDecimal.valueOf(binancePrice.getAskPrice())
                      : BigDecimal.valueOf(huobiPrice.getAsk()));
              crypto.setLastUpdated(LocalDateTime.now());
              return crypto;
            })
        .collect(Collectors.toList());
  }

  private String extractSymbol(String pair) {
    if (pair.endsWith("USDT")) {
      return pair.replace("USDT", ""); // E.g., BTCUSDT -> BTC
    }
    return pair;
  }
}
