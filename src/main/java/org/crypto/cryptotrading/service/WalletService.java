package org.crypto.cryptotrading.service;

import java.util.List;
import org.crypto.cryptotrading.entity.Wallet;
import org.crypto.cryptotrading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
  @Autowired private WalletRepository walletRepository;

  public List<Wallet> findByUserId(Long userId) {
    return walletRepository.findByUserId(userId);
  }
}
