package org.crypto.cryptotrading.service;

import org.crypto.cryptotrading.dto.Order;
import org.crypto.cryptotrading.entity.Transactions;
import org.crypto.cryptotrading.repository.OrderBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
  private final OrderBookRepository orderBookRepository;
  private final TradingService tradingService;

  public OrderService(OrderBookRepository orderBookRepository, TradingService tradingService) {
    this.orderBookRepository = orderBookRepository;
    this.tradingService = tradingService;
  }

  public Long createOrder(Order newOrder) {
    Long transactionId = null;
    String key = UUID.randomUUID().toString();
    String type = newOrder.getTypeTrading();
    BigDecimal price = newOrder.getPriceCrypto();
    BigDecimal amount = newOrder.getAmount();

    logger.info("Creating new order: type={}, price={}, amount={}", type, price, amount);

    try {
      // Determine the opposite order type
      String oppositeType = type.equals("BUY") ? "SELL" : "BUY";

      // Fetch matching orders from the order book
      Set<String> matchingOrders =
          orderBookRepository.getOrdersByTypeAndPrice(oppositeType, price, type.equals("BUY"));
      logger.info(
          "Found {} matching orders for type={}, price={}",
          matchingOrders.size(),
          oppositeType,
          price);

      boolean isSuccess = false;

      // Process matching orders
      for (String orderId : matchingOrders) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
          logger.info("No remaining amount to match. Stopping matching process.");
          break;
        }

        // Retrieve the matched order
        Order matchedOrder = orderBookRepository.getOrder(orderId);
        BigDecimal tradeAmount = amount.min(matchedOrder.getAmount());

        logger.info(
            "Matching with orderId={}, tradeAmount={}, remainingAmount={}",
            orderId,
            tradeAmount,
            amount);

        // Update amounts
        amount = amount.subtract(tradeAmount);
        matchedOrder.setAmount(matchedOrder.getAmount().subtract(tradeAmount));

        // Remove or update matched order
        if (matchedOrder.getAmount().compareTo(BigDecimal.ZERO) == 0) {
          orderBookRepository.removeOrder(orderId, matchedOrder.getTypeTrading());
          logger.info("Order {} fully matched and removed from order book.", orderId);
        } else {
          orderBookRepository.saveOrder(matchedOrder);
          logger.info(
              "Order {} partially matched. Updated remainingAmount={}",
              orderId,
              matchedOrder.getAmount());
        }

        isSuccess = true;
      }

      // If any remaining amount is unmatched, save the new order to the order book
      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        newOrder.setAmount(amount);
        newOrder.setOrderId(key);
        orderBookRepository.saveOrder(newOrder);
        logger.info("Unmatched order saved: orderId={}, remainingAmount={}", key, amount);
      }

      // Execute the trade if any matches occurred
      if (isSuccess) {
        transactionId = tradingService.executeTrade(newOrder);
        logger.info("Trade executed successfully for order: {}", newOrder);
      }

    } catch (Exception e) {
      logger.error(
          "Error occurred while processing order: type={}, price={}, amount={}",
          type,
          price,
          amount,
          e);
      throw new RuntimeException("Failed to process order.", e);
    }

    return transactionId;
  }
}
