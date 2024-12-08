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
import org.springframework.stereotype.Service;

@Service
public class PriceAggregationService {

  private static final Logger logger = LoggerFactory.getLogger(PriceAggregationService.class);

  private final BinanceService binanceService;
  private final CryptoService cryptoService;
  private final HuobiService huobiService;
  private final CryptoMapper cryptoMapper;

  @Autowired
  public PriceAggregationService(
      BinanceService binanceService,
      CryptoService cryptoService,
      HuobiService huobiService,
      CryptoMapper cryptoMapper) {
    this.binanceService = binanceService;
    this.cryptoService = cryptoService;
    this.huobiService = huobiService;
    this.cryptoMapper = cryptoMapper;
  }

  public void fetchPricesFromSources() {
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

      // Save or update each Crypto in the database
      cryptos.forEach(
          crypto -> {
            try {
              cryptoService.saveOrUpdateCrypto(crypto);
              logger.info("Successfully updated Crypto: {}", crypto);
            } catch (Exception e) {
              logger.error("Failed to save or update Crypto: {}", crypto, e);
            }
          });

      logger.info("Successfully processed {} cryptos", cryptos.size());

    } catch (Exception e) {
      // Catch and log any unexpected errors
      logger.error(
          "Unexpected error occurred while fetching and processing prices: {}", e.getMessage(), e);
    }
  }
}
