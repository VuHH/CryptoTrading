package org.crypto.cryptotrading.service;

import java.util.List;
import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.repository.CryptoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CryptoService {
  private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);
  @Autowired private CryptoRepository cryptoRepository;
  @Autowired private PriceAggregationService priceAggregationService;

  public List<Crypto> getCryptos() {
    return priceAggregationService.fetchPricesFromSources();
  }

  @Transactional
  public void saveOrUpdateCryptos(List<Crypto> cryptos) {
    for (Crypto crypto : cryptos) {
      // Fetch the existing Crypto entity by its symbol
      Crypto existingCrypto = cryptoRepository.findByCryptoSymbol(crypto.getCryptoSymbol());

      if (existingCrypto != null) {
        // Update the existing Crypto entity with new values
        existingCrypto.setBidPrice(crypto.getBidPrice());
        existingCrypto.setAskPrice(crypto.getAskPrice());
        existingCrypto.setLastUpdated(crypto.getLastUpdated());
        logger.info("Updating existing Crypto: {}", crypto.getCryptoSymbol());
      } else {
        // If no existing entity, prepare to save a new Crypto entity
        existingCrypto = crypto;
        logger.info("Creating new Crypto: {}", crypto.getCryptoSymbol());
      }

      // Save the entity (either updated or new)
      cryptoRepository.save(existingCrypto);
    }
  }

  public Crypto findCryptoBySymbol(String symbol) {
    return priceAggregationService.getCryptoBySymbol(symbol);
  }
}
