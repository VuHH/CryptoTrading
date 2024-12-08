package org.crypto.cryptotrading.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.crypto.cryptotrading.dto.BinancePrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class BinanceService {
  private static final Logger logger = LoggerFactory.getLogger(BinanceService.class);

  private final RestTemplate restTemplate;

  public BinanceService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<BinancePrice> fetchPricesFromBinance() {
    String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";
    logger.info("Calling Binance API: {}", binanceUrl);

    try {
      // Send request to Binance API and parse response into BinancePrice array
      ResponseEntity<BinancePrice[]> binanceResponse =
          restTemplate.getForEntity(binanceUrl, BinancePrice[].class);

      // Check if the HTTP response status is 2xx (successful)
      if (!binanceResponse.getStatusCode().is2xxSuccessful()) {
        logger.warn("Non-successful HTTP response: {}", binanceResponse.getStatusCode());
        return Collections.emptyList();
      }

      // Check if the response body is null or empty
      BinancePrice[] responseBody = binanceResponse.getBody();
      if (responseBody == null || responseBody.length == 0) {
        logger.warn("Empty or null response body from Binance API");
        return Collections.emptyList();
      }

      // Filter the symbols BTCUSDT and ETHUSDT from the response
      List<BinancePrice> binancePrices =
          Arrays.stream(responseBody)
              .filter(
                  price ->
                      price != null
                          && // Avoid NullPointerException
                          price.getSymbol() != null
                          && // Ensure symbol is not null
                          ("BTCUSDT".equals(price.getSymbol())
                              || "ETHUSDT".equals(price.getSymbol())))
              .collect(Collectors.toList());

      // Log a warning if no matching symbols are found
      if (binancePrices.isEmpty()) {
        logger.warn("No matching symbols found in the Binance response");
      }

      return binancePrices;

    } catch (HttpClientErrorException e) {
      // Handle HTTP client errors (4xx responses)
      logger.error("HTTP error occurred: {}", e.getMessage(), e);
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        logger.warn("Binance API returned 404 Not Found");
      } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        logger.error("Unauthorized access to Binance API");
      }
      return Collections.emptyList();

    } catch (ResourceAccessException e) {
      // Handle resource access errors (e.g., timeouts, connection failures)
      logger.error("Resource access error: {}", e.getMessage(), e);
      return Collections.emptyList();

    } catch (Exception e) {
      // Catch and log any unexpected errors
      logger.error("Unexpected error occurred: {}", e.getMessage(), e);
      return Collections.emptyList();
    }
  }
}
