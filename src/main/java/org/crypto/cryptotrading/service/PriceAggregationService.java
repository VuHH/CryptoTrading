package org.crypto.cryptotrading.service;

import org.crypto.cryptotrading.dto.BinancePrice;
import org.crypto.cryptotrading.dto.HuobiPrice;
import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PriceAggregationService {

    private final BinanceService binanceService;
    private final CryptoRepository cryptoRepository;
    private final HuobiService huobiService;

    @Autowired
    public PriceAggregationService(BinanceService binanceService, CryptoRepository cryptoRepository, HuobiService huobiService) {
        this.binanceService = binanceService;
        this.cryptoRepository = cryptoRepository;
        this.huobiService = huobiService;
    }

    @Scheduled(fixedRate = 10000) // 10 seconds
    public void fetchAndUpdateBidPrices() {
        List<Crypto> cryptos = fetchPricesFromSources();
        cryptoRepository.saveAll(cryptos);
    }

    private List<Crypto> fetchPricesFromSources() {
        // Get AskPrice
        Crypto crypto = new Crypto();
        // Get BidPrice
        List<BinancePrice> binanceServices = binanceService.fetchBidPricesFromSources();
        //Get Ask
        List<HuobiPrice> huobiPrices = huobiService.fetchAskPricesFromSources();


    }

    private List<Crypto> fetchBidPricesFromSources() {
        // Example for Binance

        // Parse and aggregate prices from Binance and Huobi
        // Use logic to compare bid/ask prices and choose the best
        return aggregatedPrices;
    }

}
