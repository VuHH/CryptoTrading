package org.crypto.cryptotrading.controller;

import java.util.List;
import org.crypto.cryptotrading.entity.Wallet;
import org.crypto.cryptotrading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired private WalletRepository walletRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Wallet>> getWalletBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(walletRepository.findByUserId(userId));
    }

}
