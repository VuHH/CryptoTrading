package org.crypto.cryptotrading.controller;

import jakarta.validation.Valid;
import java.util.Map;
import org.crypto.cryptotrading.dto.TradingRequest;
import org.crypto.cryptotrading.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradingController {
  private final TradingService tradingService;

  @Autowired
  public TradingController(TradingService tradingService) {
    this.tradingService = tradingService;
  }

  @PostMapping
  public ResponseEntity<?> executeTrade(@Valid @RequestBody TradingRequest tradingRequest) {
    try {
      Long transactionId = tradingService.executeTrade(tradingRequest);
      return ResponseEntity.ok()
          .body(
              Map.of(
                  "status", "SUCCESS",
                  "message", "Trade executed successfully",
                  "transactionId", transactionId));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("status", "FAILED", "message", e.getMessage()));
    }
  }
}
