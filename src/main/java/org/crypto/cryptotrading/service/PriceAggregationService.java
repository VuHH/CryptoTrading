package org.crypto.cryptotrading.service;

import org.crypto.cryptotrading.dto.BinancePrice;
import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.dto.mapper.CryptoMapper;
import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriceAggregationService {

  private final BinanceService binanceService;
  private final CryptoRepository cryptoRepository;
  private final HuobiService huobiService;
  private final CryptoMapper cryptoMapper;

  @Autowired
  public PriceAggregationService(
      BinanceService binanceService,
      CryptoRepository cryptoRepository,
      HuobiService huobiService,
      CryptoMapper cryptoMapper) {
    this.binanceService = binanceService;
    this.cryptoRepository = cryptoRepository;
    this.huobiService = huobiService;
    this.cryptoMapper = cryptoMapper;
  }

  @Scheduled(fixedRate = 10000)
  private void fetchPricesFromSources() {
    // Get BidPrice
    List<BinancePrice> binanceServices = binanceService.fetchBidPricesFromSources();
    // Get Ask
    List<HuobiPrice> huobiPrices = huobiService.fetchAskPricesFromSources();

    List<Crypto> cryptos = cryptoMapper.mapPricesToCrypto(binanceServices, huobiPrices);

    cryptos.forEach(this::saveOrUpdateCrypto);
  }

  private void saveOrUpdateCrypto(Crypto crypto) {
    Crypto existingCrypto = cryptoRepository.findByCryptoSymbol(crypto.getCryptoSymbol());
    if (existingCrypto != null) {
      existingCrypto.setBidPrice(crypto.getBidPrice());
      existingCrypto.setAskPrice(crypto.getAskPrice());
      existingCrypto.setLastUpdated(crypto.getLastUpdated());
      cryptoRepository.save(existingCrypto);
    } else {
      cryptoRepository.save(crypto);
    }
  }
}
