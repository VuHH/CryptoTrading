package org.crypto.cryptotrading;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import org.crypto.cryptotrading.dto.BinancePrice;
import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.dto.mapper.CryptoMapper;
import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.service.BinanceService;
import org.crypto.cryptotrading.service.CryptoService;
import org.crypto.cryptotrading.service.HuobiService;
import org.crypto.cryptotrading.service.PriceAggregationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
public class CryptoServiceTest {

  @Autowired private CryptoService cryptoService;
  @Autowired private CryptoMapper cryptoMapper;

  @Autowired private BinanceService binanceService;
  @Autowired private HuobiService huobiService;
  @Autowired private PriceAggregationService priceAggregationService;

  @Test
  void testCryptoSaveDatabase() {
    // Check get response from huobi and binance
    List<HuobiPrice> huobiPriceList = huobiService.fetchPricesFromHuobi();
    assertEquals(2, huobiPriceList.size());

    List<BinancePrice> binancePriceList = binanceService.fetchPricesFromBinance();
    assertEquals(2, binancePriceList.size());
    // Save Database
    List<Crypto> cryptoList = cryptoMapper.mapPricesToCrypto(binancePriceList, huobiPriceList);
    cryptoService.saveOrUpdateCryptos(cryptoList);

    List<Crypto> cryptos = cryptoService.getCryptos();
    assertEquals(2, cryptos.size());
  }

  @Test
  void testRetrieveBestAggregatedPrice() {
    // Fetch prices from Huobi and Binance
    List<HuobiPrice> huobiPriceList = huobiService.fetchPricesFromHuobi();
    List<BinancePrice> binancePriceList = binanceService.fetchPricesFromBinance();

    // Extract BTC prices from Huobi and Binance
    BigDecimal bidHuobi = extractPrice(huobiPriceList, "btcusdt", HuobiPrice::getBid);
    BigDecimal askHuobi = extractPrice(huobiPriceList, "btcusdt", HuobiPrice::getAsk);
    BigDecimal bidBinance = extractPrice(binancePriceList, "BTCUSDT", BinancePrice::getBidPrice);
    BigDecimal askBinance = extractPrice(binancePriceList, "BTCUSDT", BinancePrice::getAskPrice);

    // Calculate best aggregated prices
    BigDecimal bestBid = bidHuobi.max(bidBinance); // Higher bid is better for sellers
    BigDecimal bestAsk = askHuobi.min(askBinance); // Lower ask is better for buyers

    // Save to DB
    List<Crypto> cryptoList = cryptoMapper.mapPricesToCrypto(binancePriceList, huobiPriceList);
    cryptoService.saveOrUpdateCryptos(cryptoList);

    // Validate the results
    Crypto cryptoBTC = cryptoService.findCryptoBySymbol("BTC");
    assertEquals(0, bestBid.compareTo(cryptoBTC.getBidPrice()));
    assertEquals(0, bestAsk.compareTo(cryptoBTC.getAskPrice()));
  }

  private <T> BigDecimal extractPrice(
      List<T> priceList, String symbol, Function<T, Double> priceExtractor) {
    return priceList.stream()
        .filter(
            price -> {
              if (price instanceof HuobiPrice) {
                return ((HuobiPrice) price).getSymbol().equalsIgnoreCase(symbol);
              } else if (price instanceof BinancePrice) {
                return ((BinancePrice) price).getSymbol().equalsIgnoreCase(symbol);
              }
              return false;
            })
        .map(priceExtractor)
        .map(BigDecimal::valueOf)
        .findFirst()
        .orElse(BigDecimal.ZERO);
  }
}
