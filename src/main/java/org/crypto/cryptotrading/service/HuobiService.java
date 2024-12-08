package org.crypto.cryptotrading.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.dto.HuobiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class HuobiService {
  private static final Logger logger = LoggerFactory.getLogger(HuobiService.class);
  private final RestTemplate restTemplate;

  public HuobiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<HuobiPrice> fetchPricesFromHuobi() {
    String huobiUrl = "https://api.huobi.pro/market/tickers";
    logger.info("Calling Huobi API: {}", huobiUrl);

    try {
      // Send a request to the Huobi API and parse the response into a HuobiResponse object
      HuobiResponse huobiResponse = restTemplate.getForObject(huobiUrl, HuobiResponse.class);

      // Check if the response is null or contains no data
      if (huobiResponse == null || huobiResponse.getData() == null) {
        logger.error("No data received from Huobi API");
        return Collections.emptyList();
      }

      // Filter the symbols "btcusdt" and "ethusdt" from the response data
      List<HuobiPrice> huobiPrices =
          huobiResponse.getData().stream()
              .filter(
                  price ->
                      price != null
                          && // Avoid NullPointerException
                          price.getSymbol() != null
                          && // Ensure the symbol is not null
                          ("btcusdt".equalsIgnoreCase(price.getSymbol())
                              || "ethusdt".equalsIgnoreCase(price.getSymbol())))
              .collect(Collectors.toList());

      // Log a warning if no matching symbols are found
      if (huobiPrices.isEmpty()) {
        logger.warn("No matching symbols found in the Huobi response");
      }

      return huobiPrices;

    } catch (HttpClientErrorException e) {
      // Handle HTTP client errors (4xx responses)
      logger.error("HTTP error occurred: {}", e.getMessage(), e);
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        logger.warn("Huobi API returned 404 Not Found");
      } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        logger.error("Unauthorized access to Huobi API");
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
