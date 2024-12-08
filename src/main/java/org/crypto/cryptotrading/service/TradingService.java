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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TradingService {
  private static final Logger logger = LoggerFactory.getLogger(TradingService.class);

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
    logger.info("Starting trade execution for userId: {}, symbol: {}, type: {}, amount: {}",
            tradeRequest.getUserId(), tradeRequest.getSymbol(), tradeRequest.getTypeTrading(), tradeRequest.getAmount());

    // Validate trading request details
    String symbol = tradeRequest.getSymbol();
    String typeTrade = tradeRequest.getTypeTrading().toUpperCase();
    BigDecimal amount = tradeRequest.getAmount();

    // Fetch crypto details
    Crypto crypto = cryptoRepository.findByCryptoSymbol(symbol);
    if (crypto == null) {
      logger.error("Invalid crypto symbol: {}", symbol);
      throw new IllegalArgumentException("Invalid trading crypto: " + symbol);
    }
    logger.info("Fetched crypto details for symbol: {}, bidPrice: {}, askPrice: {}",
            symbol, crypto.getBidPrice(), crypto.getAskPrice());

    // Determine the price based on trade type
    BigDecimal priceCrypto = "BUY".equals(typeTrade) ? crypto.getAskPrice() : crypto.getBidPrice();
    BigDecimal totalCost = amount.multiply(priceCrypto);
    logger.info("Calculated total cost: {} for trade type: {}", totalCost, typeTrade);

    // Fetch wallet and user details
    Wallet cryptoWallet = walletRepository.findByUserIdAndCryptoSymbol(tradeRequest.getUserId(), symbol);
    if (cryptoWallet == null) {
      logger.error("Crypto wallet not found for userId: {}, symbol: {}", tradeRequest.getUserId(), symbol);
      throw new IllegalStateException("Crypto wallet not found for userId: " + tradeRequest.getUserId());
    }

    User userWallet = userRepository.findById(tradeRequest.getUserId())
            .orElseThrow(() -> {
              logger.error("User not found with userId: {}", tradeRequest.getUserId());
              return new IllegalStateException("User not found: " + tradeRequest.getUserId());
            });
    logger.info("Fetched wallet details for userId: {}", tradeRequest.getUserId());

    // Perform trade
    if ("BUY".equals(typeTrade)) {
      logger.info("Processing BUY trade for userId: {}, symbol: {}, amount: {}", tradeRequest.getUserId(), symbol, amount);
      processBuyTrade(userWallet, cryptoWallet, amount, totalCost);
    } else if ("SELL".equals(typeTrade)) {
      logger.info("Processing SELL trade for userId: {}, symbol: {}, amount: {}", tradeRequest.getUserId(), symbol, amount);
      processSellTrade(userWallet, cryptoWallet, amount, totalCost);
    } else {
      logger.error("Invalid trade type: {}", typeTrade);
      throw new IllegalArgumentException("Invalid trade type: " + typeTrade);
    }

    // Save wallet and user updates
    walletRepository.save(cryptoWallet);
    userRepository.save(userWallet);
    logger.info("Updated wallet and user balances successfully for userId: {}", tradeRequest.getUserId());

    // Record the transaction
    Transactions transaction = recordTransaction(tradeRequest.getUserId(), symbol, amount, typeTrade, totalCost);
    logger.info("Recorded transaction with ID: {}", transaction.getId());

    return transaction.getId();
  }

  private void processBuyTrade(User userWallet, Wallet cryptoWallet, BigDecimal amount, BigDecimal totalCost) {
    if (userWallet.getWalletBalanceUsdt().compareTo(totalCost) < 0) {
      logger.error("Insufficient USDT balance for userId: {}, required: {}, available: {}",
              userWallet.getId(), totalCost, userWallet.getWalletBalanceUsdt());
      throw new RuntimeException("Insufficient USDT balance");
    }
    userWallet.setWalletBalanceUsdt(userWallet.getWalletBalanceUsdt().subtract(totalCost));
    cryptoWallet.setBalance(cryptoWallet.getBalance().add(amount));
    logger.info("BUY trade completed for userId: {}, symbol: {}, amount: {}, totalCost: {}",
            userWallet.getId(), cryptoWallet.getCryptoSymbol(), amount, totalCost);
  }

  private void processSellTrade(User userWallet, Wallet cryptoWallet, BigDecimal amount, BigDecimal totalCost) {
    if (cryptoWallet.getBalance().compareTo(amount) < 0) {
      logger.error("Insufficient crypto balance for userId: {}, symbol: {}, required: {}, available: {}",
              userWallet.getId(), cryptoWallet.getCryptoSymbol(), amount, cryptoWallet.getBalance());
      throw new RuntimeException("Insufficient crypto balance");
    }
    cryptoWallet.setBalance(cryptoWallet.getBalance().subtract(amount));
    userWallet.setWalletBalanceUsdt(userWallet.getWalletBalanceUsdt().add(totalCost));
    logger.info("SELL trade completed for userId: {}, symbol: {}, amount: {}, totalCost: {}",
            userWallet.getId(), cryptoWallet.getCryptoSymbol(), amount, totalCost);
  }

  private Transactions recordTransaction(Long userId, String symbol, BigDecimal amount, String typeTrade, BigDecimal totalCost) {
    Transactions transaction = new Transactions();
    transaction.setUserId(userId);
    transaction.setCryptoSymbol(symbol);
    transaction.setAmount(amount);
    transaction.setTransactionType(typeTrade);
    transaction.setPrice(totalCost);
    transaction.setTimestamp(LocalDateTime.now());
    Transactions savedTransaction = transactionsRepository.save(transaction);
    logger.info("Transaction recorded successfully for userId: {}, symbol: {}, type: {}, amount: {}, totalCost: {}",
            userId, symbol, typeTrade, amount, totalCost);
    return savedTransaction;
  }

}
