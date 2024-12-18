package org.crypto.cryptotrading.controller;

import java.util.List;
import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
public class CrytoController {
  @Autowired private CryptoService cryptoService;

  @GetMapping
  public List<Crypto> getPriceCrypto() {
    return cryptoService.getCryptos();
  }

  @GetMapping("/{cryptoSymbol}")
  public ResponseEntity<Crypto> getLatestPrice(@PathVariable String cryptoSymbol) {
    return ResponseEntity.ok(cryptoService.findCryptoBySymbol(cryptoSymbol));
  }
}
