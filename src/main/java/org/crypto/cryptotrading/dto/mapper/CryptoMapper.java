package org.crypto.cryptotrading.dto.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.crypto.cryptotrading.dto.BinancePrice;
import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.entity.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CryptoMapper {
  private static final Logger logger = LoggerFactory.getLogger(CryptoMapper.class);

  public List<Crypto> mapPricesToCrypto(
      List<BinancePrice> binancePrices, List<HuobiPrice> huobiPrices) {

    // Create a map of Huobi prices for quick lookup by symbol (case-insensitive)
    Map<String, HuobiPrice> huobiPriceMap =
        huobiPrices.stream()
            .collect(
                Collectors.toMap(
                    huobiPrice ->
                        huobiPrice.getSymbol().toLowerCase(), // Normalize to lowercase for matching
                    huobiPrice -> huobiPrice));

    // Filter and map Binance prices to Crypto objects
    return binancePrices.stream()
        .filter(
            binancePrice ->
                binancePrice != null
                    && ("BTCUSDT".equals(binancePrice.getSymbol())
                        || "ETHUSDT".equals(binancePrice.getSymbol())))
        .map(
            binancePrice -> {
              // Find corresponding HuobiPrice (case-insensitive)
              HuobiPrice huobiPrice = huobiPriceMap.get(binancePrice.getSymbol().toLowerCase());

              // Skip processing if the corresponding Huobi price is not found
              if (huobiPrice == null) {
                logger.warn("No matching Huobi price for symbol: {}", binancePrice.getSymbol());
                return null;
              }

              // Create and populate a Crypto object
              Crypto crypto = new Crypto();
              crypto.setCryptoSymbol(extractSymbol(binancePrice.getSymbol()));
              crypto.setBidPrice(
                  BigDecimal.valueOf(
                      Math.max(
                          binancePrice.getBidPrice(),
                          huobiPrice.getBid()) // Best aggregated SELL price
                      ));
              crypto.setAskPrice(
                  BigDecimal.valueOf(
                      Math.min(
                          binancePrice.getAskPrice(),
                          huobiPrice.getAsk()) // Best aggregated BUY price
                      ));
              crypto.setLastUpdated(LocalDateTime.now());

              return crypto;
            })
        .filter(Objects::nonNull) // Remove null entries (if any HuobiPrice was missing)
        .collect(Collectors.toList());
  }

  private String extractSymbol(String pair) {
    if (pair.endsWith("USDT")) {
      return pair.replace("USDT", ""); // E.g., BTCUSDT -> BTC
    }
    return pair;
  }
}
