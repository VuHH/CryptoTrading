package org.crypto.cryptotrading.controller;

import jakarta.validation.Valid;
import java.util.Map;
import org.crypto.cryptotrading.dto.Order;
import org.crypto.cryptotrading.service.OrderService;
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
  private final OrderService orderService;
  private final TradingService tradingService;

  @Autowired
  public TradingController(OrderService orderService, TradingService tradingService) {
    this.orderService = orderService;
    this.tradingService = tradingService;
  }

  @PostMapping
  public ResponseEntity<?> executeTrade(@Valid @RequestBody Order order) {
    try {
      Long transactionId = tradingService.executeTrade(order);
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

  //  @PostMapping
  //  public ResponseEntity<?> executeOrder(@Valid @RequestBody Order order) {
  //    try {
  //      Long transactionId = orderService.createOrder(order);
  //      return ResponseEntity.ok()
  //              .body(
  //                      Map.of(
  //                              "status", "SUCCESS",
  //                              "message", "Trade executed successfully",
  //                              "transactionId", transactionId));
  //    } catch (RuntimeException e) {
  //      return ResponseEntity.badRequest()
  //              .body(Map.of("status", "FAILED", "message", e.getMessage()));
  //    }
  //  }
}
