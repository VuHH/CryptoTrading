package org.crypto.cryptotrading.schedule;

import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.service.CryptoService;
import org.crypto.cryptotrading.service.PriceAggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true)
@EnableScheduling
public class PriceAggregationScheduled {

  private final PriceAggregationService priceAggregationService;
  private final CryptoService cryptoService;

  @Autowired
  public PriceAggregationScheduled(
      PriceAggregationService priceAggregationService, CryptoService cryptoService) {
    this.priceAggregationService = priceAggregationService;
    this.cryptoService = cryptoService;
  }

  @Scheduled(fixedRate = 10000)
  @CacheEvict(value = "cryptoPrices", key = "'latestPrices'")
  public void scheduleFetchPricesFromSources() {
    List<Crypto> cryptoList = priceAggregationService.fetchPricesFromSources();
    // Save or update each Crypto in the database
    cryptoService.saveOrUpdateCryptos(cryptoList);
  }
}
