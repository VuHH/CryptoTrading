package org.crypto.cryptotrading.service;

import org.crypto.cryptotrading.dto.BinancePrice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BinanceService {

  private final RestTemplate restTemplate;

  public BinanceService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<BinancePrice> fetchBidPricesFromSources() {
    String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
    ResponseEntity<BinancePrice[]> binanceResponse =
        restTemplate.getForEntity(binanceUrl, BinancePrice[].class);
    if (binanceResponse.getBody() == null || binanceResponse.getBody().length == 0) {
      throw new RuntimeException("No data received from Binance API");
    }
    return Arrays.stream(binanceResponse.getBody())
        .filter(price -> "BTCUSDT".equals(price.getSymbol()) || "ETHUSDT".equals(price.getSymbol()))
        .collect(Collectors.toList());
  }
}
