package org.crypto.cryptotrading.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.crypto.cryptotrading.dto.TradingRequest;
import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.entity.Transactions;
import org.crypto.cryptotrading.entity.User;
import org.crypto.cryptotrading.entity.Wallet;
import org.crypto.cryptotrading.repository.CryptoRepository;
import org.crypto.cryptotrading.repository.TransactionsRepository;
import org.crypto.cryptotrading.repository.UserRepository;
import org.crypto.cryptotrading.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class TradingService {
  private final TransactionsRepository transactionsRepository;
  private final CryptoRepository cryptoRepository;
  private final WalletRepository walletRepository;
  private final UserRepository userRepository;

  public TradingService(
          TransactionsRepository transactionsRepository,
          CryptoRepository cryptoRepository,
          WalletRepository walletRepository, UserRepository userRepository) {
    this.transactionsRepository = transactionsRepository;
    this.cryptoRepository = cryptoRepository;
    this.walletRepository = walletRepository;
      this.userRepository = userRepository;
  }

  @Transactional
  public Long executeTrade(TradingRequest tradeRequest) {
    String symbol = tradeRequest.getSymbol();
    String typeTrade = tradeRequest.getTypeTrading();
    BigDecimal amount = tradeRequest.getAmount();

    Crypto crypto = cryptoRepository.findByCryptoSymbol(symbol);

    if (crypto == null) {
      throw new RuntimeException("Invalid trading crypto");
    }

    BigDecimal priceCrypto = typeTrade.equals("BUY") ? crypto.getAskPrice() : crypto.getBidPrice();
    BigDecimal totalCost = amount.multiply(priceCrypto);

    Wallet cryptoWallet = walletRepository.findByUserIdAndCryptoSymbol(tradeRequest.getUserId(), symbol);
    User userWallet = userRepository.findById(tradeRequest.getUserId());

    if (typeTrade.equals("BUY")) {
      if (userWallet.getWalletBalanceUsdt().compareTo(totalCost) < 0) {
        throw new RuntimeException("Insufficient USDT balance");
      }
      userWallet.setWalletBalanceUsdt(userWallet.getWalletBalanceUsdt().subtract(totalCost));
      cryptoWallet.setBalance(cryptoWallet.getBalance().add(amount));
    } else if (typeTrade.equals("SELL")) {
      if (cryptoWallet.getBalance().compareTo(amount) < 0) {
        throw new RuntimeException("Insufficient crypto balance");
      }
      cryptoWallet.setBalance(cryptoWallet.getBalance().subtract(amount));
      userWallet.setWalletBalanceUsdt(userWallet.getWalletBalanceUsdt().add(totalCost));
    }

    walletRepository.save(cryptoWallet);
    userRepository.save(userWallet);

    Transactions transaction = new Transactions();
    transaction.setUserId(tradeRequest.getUserId());
    transaction.setCryptoSymbol(symbol);
    transaction.setAmount(amount);
    transaction.setTransactionType(typeTrade);
    transaction.setPrice(totalCost);
    transaction.setTimestamp(LocalDateTime.now());
    transaction = transactionsRepository.save(transaction);
    return transaction.getId();
  }
}
