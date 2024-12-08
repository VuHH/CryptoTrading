package org.crypto.cryptotrading.service;

import java.util.Collections;
import java.util.List;
import org.crypto.cryptotrading.dto.BinancePrice;
import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.dto.mapper.CryptoMapper;
import org.crypto.cryptotrading.entity.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PriceAggregationService {

  private static final Logger logger = LoggerFactory.getLogger(PriceAggregationService.class);

  private final BinanceService binanceService;
  private final HuobiService huobiService;
  private final CryptoMapper cryptoMapper;

  @Autowired
  public PriceAggregationService(
      BinanceService binanceService,
      HuobiService huobiService,
      CryptoMapper cryptoMapper) {
    this.binanceService = binanceService;
    this.huobiService = huobiService;
    this.cryptoMapper = cryptoMapper;
  }

  @Cacheable(
      value = "cryptoPrices",
      key = "'latestPrices'",
      unless = "#result == null || #result.isEmpty()")
  public List<Crypto> fetchPricesFromSources() {
    logger.info("Fetching prices from Binance and Huobi sources...");

    try {
      // Fetch bid prices from Binance
      List<BinancePrice> binancePrices = binanceService.fetchPricesFromBinance();
      if (binancePrices == null || binancePrices.isEmpty()) {
        logger.warn("No prices fetched from Binance service");
        binancePrices = Collections.emptyList();
      }

      // Fetch ask prices from Huobi
      List<HuobiPrice> huobiPrices = huobiService.fetchPricesFromHuobi();
      if (huobiPrices == null || huobiPrices.isEmpty()) {
        logger.warn("No prices fetched from Huobi service");
        huobiPrices = Collections.emptyList();
      }

      // Map Binance and Huobi prices to Crypto objects
      List<Crypto> cryptos = cryptoMapper.mapPricesToCrypto(binancePrices, huobiPrices);
      if (cryptos == null || cryptos.isEmpty()) {
        logger.warn("No cryptos were mapped from Binance and Huobi prices");
      }
      logger.info("Successfully processed {} cryptos", cryptos.size());
      return cryptos;

    } catch (Exception e) {
      // Catch and log any unexpected errors
      logger.error(
          "Unexpected error occurred while fetching and processing prices: {}", e.getMessage(), e);
      return Collections.emptyList();
    }
  }

  @Cacheable(value = "cryptoPrices", key = "#symbol", unless = "#result == null")
  public Crypto getCryptoBySymbol(String symbol) {
    List<Crypto> cryptos = fetchPricesFromSources();
    return cryptos.stream()
        .filter(crypto -> crypto.getCryptoSymbol().equals(symbol))
        .findFirst()
        .orElse(null);
  }
}
