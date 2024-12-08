package org.crypto.cryptotrading;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import org.awaitility.Durations;
import org.crypto.cryptotrading.schedule.PriceAggregationScheduled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
class PriceAggregationTests {

  @MockitoSpyBean
  private final PriceAggregationScheduled priceAggregationScheduled =
      mock(PriceAggregationScheduled.class);

  @Test
  void testScheduledAggregateBestPrices() {
    // Test Scheduled after 10 seconds
    new Thread(priceAggregationScheduled::scheduleFetchPricesFromSources).start();

    await()
        .atMost(Durations.TEN_SECONDS)
        .untilAsserted(
            () -> {
              verify(priceAggregationScheduled, atLeastOnce()).scheduleFetchPricesFromSources();
            });
  }
}
