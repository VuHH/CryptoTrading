package org.crypto.cryptotrading.schedule;

import org.crypto.cryptotrading.service.PriceAggregationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@ConditionalOnProperty(value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true)
@EnableScheduling
public class PriceAggregationScheduled {

  private final PriceAggregationService priceAggregationService;

  @Autowired
  public PriceAggregationScheduled(PriceAggregationService priceAggregationService) {
    this.priceAggregationService = priceAggregationService;
  }

  @Scheduled(fixedRate = 10000)
  public void scheduleFetchPricesFromSources() {
    priceAggregationService.fetchPricesFromSources();
  }
}
