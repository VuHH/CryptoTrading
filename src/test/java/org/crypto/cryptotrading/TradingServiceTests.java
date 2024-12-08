package org.crypto.cryptotrading;

import org.crypto.cryptotrading.dto.Order;
import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.entity.User;
import org.crypto.cryptotrading.entity.Wallet;
import org.crypto.cryptotrading.repository.CryptoRepository;
import org.crypto.cryptotrading.repository.TransactionsRepository;
import org.crypto.cryptotrading.repository.UserRepository;
import org.crypto.cryptotrading.repository.WalletRepository;
import org.crypto.cryptotrading.service.PriceAggregationService;
import org.crypto.cryptotrading.service.TradingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
class TradingServiceTests {

  private CryptoRepository cryptoRepository;
  private WalletRepository walletRepository;
  private UserRepository userRepository;
  private TradingService tradingService;
  @Autowired private TransactionsRepository transactionsRepository;
  @Autowired private PriceAggregationService priceAggregationService;

  @BeforeEach
  void setUp() {
    cryptoRepository = mock(CryptoRepository.class);
    walletRepository = mock(WalletRepository.class);
    userRepository = mock(UserRepository.class);
    tradingService =
        new TradingService(
            transactionsRepository,
            walletRepository,
            userRepository,
            priceAggregationService);
  }

  @Test
  void testExecuteBuyTrade_Success() {
    Order request = new Order();
    request.setUserId(1L);
    request.setSymbol("BTC");
    request.setTypeTrading("BUY");
    request.setAmount(new BigDecimal("0.01"));

    Crypto crypto =
        new Crypto(
            1L, "BTC", new BigDecimal("50000"), new BigDecimal("50100"), LocalDateTime.now());
    Wallet wallet = new Wallet(1L, 1L, "BTC", new BigDecimal("0.5"));
    User user = new User(1L, "Vu", new BigDecimal("100000"));

    when(cryptoRepository.findByCryptoSymbol("BTC")).thenReturn(crypto);
    when(walletRepository.findByUserIdAndCryptoSymbol(1L, "BTC")).thenReturn(wallet);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    Long transactionId = tradingService.executeTrade(request);

    verify(walletRepository).save(wallet);
    verify(userRepository).save(user);

    assertNotNull(transactionId);
  }

  @Test
  void testExecuteTrade_InvalidSymbol() {
    Order request = new Order();
    request.setUserId(1L);
    request.setSymbol("INVALID");
    request.setTypeTrading("BUY");
    request.setAmount(new BigDecimal("0.01"));

    when(cryptoRepository.findByCryptoSymbol("INVALID")).thenReturn(null);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> tradingService.executeTrade(request));

    assertEquals("Invalid trading crypto: INVALID", exception.getMessage());
  }

  @Test
  void testExecuteTrade_WalletNotFound() {
    Order request = new Order();
    request.setUserId(1L);
    request.setSymbol("BTC");
    request.setTypeTrading("BUY");
    request.setAmount(new BigDecimal("0.01"));

    Crypto crypto =
        new Crypto(
            1L, "BTC", new BigDecimal("50000"), new BigDecimal("50100"), LocalDateTime.now());
    when(cryptoRepository.findByCryptoSymbol("BTC")).thenReturn(crypto);
    when(walletRepository.findByUserIdAndCryptoSymbol(1L, "BTC")).thenReturn(null);

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> tradingService.executeTrade(request));

    assertEquals("Crypto wallet not found for userId: 1", exception.getMessage());
  }

  @Test
  void testExecuteTrade_UserNotFound() {
    Order request = new Order();
    request.setUserId(1L);
    request.setSymbol("BTC");
    request.setTypeTrading("BUY");
    request.setAmount(new BigDecimal("0.01"));

    Crypto crypto =
        new Crypto(
            1L, "BTC", new BigDecimal("50000"), new BigDecimal("50100"), LocalDateTime.now());
    Wallet wallet = new Wallet(1L, 1L, "BTC", new BigDecimal("0.5"));

    when(cryptoRepository.findByCryptoSymbol("BTC")).thenReturn(crypto);
    when(walletRepository.findByUserIdAndCryptoSymbol(1L, "BTC")).thenReturn(wallet);
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> tradingService.executeTrade(request));

    assertEquals("User not found: 1", exception.getMessage());
  }

  @Test
  void testExecuteTrade_InvalidTradeType() {
    Order request = new Order();
    request.setUserId(1L);
    request.setSymbol("BTC");
    request.setTypeTrading("INVALID");
    request.setAmount(new BigDecimal("0.01"));

    Crypto crypto =
        new Crypto(
            1L, "BTC", new BigDecimal("50000"), new BigDecimal("50100"), LocalDateTime.now());
    Wallet wallet = new Wallet(1L, 1L, "BTC", new BigDecimal("0.5"));
    User user = new User(1L, "Vu", new BigDecimal("100000"));
    when(cryptoRepository.findByCryptoSymbol("BTC")).thenReturn(crypto);
    when(walletRepository.findByUserIdAndCryptoSymbol(1L, "BTC")).thenReturn(wallet);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> tradingService.executeTrade(request));

    assertEquals("Invalid trade type: INVALID", exception.getMessage());
  }
}
