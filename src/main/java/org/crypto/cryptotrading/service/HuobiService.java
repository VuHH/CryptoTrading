package org.crypto.cryptotrading.service;

import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.dto.HuobiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HuobiService {
  private final RestTemplate restTemplate;

  public HuobiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<HuobiPrice> fetchAskPricesFromSources() {
    String huobiUrl = "https://api.huobi.pro/market/tickers";
    HuobiResponse huobiResponse =
        restTemplate.getForObject(huobiUrl, HuobiResponse.class);
    if (huobiResponse == null) {
      throw new RuntimeException("No data received from Binance API");
    }
    return huobiResponse.getData().stream()
        .filter(price -> "btcusdt".equals(price.getSymbol()) || "ethusdt".equals(price.getSymbol()))
        .collect(Collectors.toList());
  }
}
