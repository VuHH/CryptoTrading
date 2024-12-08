package org.crypto.cryptotrading.controller;

import java.util.List;
import org.crypto.cryptotrading.entity.Transactions;
import org.crypto.cryptotrading.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
  @Autowired private TransactionService transactionService;

  @GetMapping("/{userId}")
  public ResponseEntity<List<Transactions>> getUserTradingHistory(@PathVariable Long userId) {
    return ResponseEntity.ok(transactionService.findByUserId(userId));
  }
}
