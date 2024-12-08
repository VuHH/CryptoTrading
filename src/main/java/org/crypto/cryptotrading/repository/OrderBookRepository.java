package org.crypto.cryptotrading.repository;

import org.crypto.cryptotrading.dto.Order;
import org.crypto.cryptotrading.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Repository
public class OrderBookRepository {
  private static final Logger logger = LoggerFactory.getLogger(OrderBookRepository.class);
  private final RedisTemplate<String, String> redisTemplate;

  public OrderBookRepository(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void saveOrder(Order order) {
    if (order == null || order.getOrderId() == null) {
      throw new IllegalArgumentException("Order or Order ID cannot be null");
    }

    String key = order.getOrderId();
    String orderKey = "order:" + key;
    String zSetKey = order.getTypeTrading().equals("BUY") ? "buy_orders" : "sell_orders";

    logger.info(
        "Saving order: orderId={}, type={}, price={}, amount={}",
        order.getOrderId(),
        order.getTypeTrading(),
        order.getPriceCrypto(),
        order.getAmount());

    // Save order details in Hash
    redisTemplate.opsForHash().put(orderKey, "userId", String.valueOf(order.getUserId()));
    redisTemplate.opsForHash().put(orderKey, "type", order.getTypeTrading());
    redisTemplate.opsForHash().put(orderKey, "symbol", order.getSymbol());
    redisTemplate.opsForHash().put(orderKey, "price", String.valueOf(order.getPriceCrypto()));
    redisTemplate.opsForHash().put(orderKey, "amount", String.valueOf(order.getAmount()));

    // Save order in Sorted Set
    redisTemplate.opsForZSet().add(zSetKey, key, order.getPriceCrypto().doubleValue());

    logger.info("Order saved successfully: orderId={}", order.getOrderId());
  }

  public void removeOrder(String orderId, String type) {
    if (orderId == null || type == null) {
      throw new IllegalArgumentException("Order ID and type cannot be null");
    }

    String orderKey = "order:" + orderId;
    String zSetKey = type.equals("BUY") ? "buy_orders" : "sell_orders";

    logger.info("Removing order: orderId={}, type={}", orderId, type);

    // Remove from Hash and Sorted Set
    redisTemplate.delete(orderKey);
    redisTemplate.opsForZSet().remove(zSetKey, orderId);

    logger.info("Order removed successfully: orderId={}", orderId);
  }

  public Order getOrder(String orderId) {
    if (orderId == null) {
      throw new IllegalArgumentException("Order ID cannot be null");
    }

    String orderKey = "order:" + orderId;

    logger.info("Fetching order: orderId={}", orderId);

    // Fetch order details from Redis
    String type = (String) redisTemplate.opsForHash().get(orderKey, "type");
    String symbol = (String) redisTemplate.opsForHash().get(orderKey, "symbol");
    String userIdStr = (String) redisTemplate.opsForHash().get(orderKey, "userId");
    String priceStr = (String) redisTemplate.opsForHash().get(orderKey, "price");
    String amountStr = (String) redisTemplate.opsForHash().get(orderKey, "amount");

    if (type == null
        || symbol == null
        || userIdStr == null
        || priceStr == null
        || amountStr == null) {
      throw new IllegalStateException("Incomplete order data for orderId=" + orderId);
    }

    Long userId = Long.valueOf(userIdStr);
    BigDecimal price = new BigDecimal(priceStr);
    BigDecimal amount = new BigDecimal(amountStr);

    logger.info(
        "Order fetched successfully: orderId={}, type={}, price={}, amount={}",
        orderId,
        type,
        price,
        amount);

    return new Order(orderId, userId, symbol, type, price, amount);
  }

  public Set<String> getOrdersByTypeAndPrice(String type, BigDecimal price, boolean isLessThan) {
    if (type == null || price == null) {
      throw new IllegalArgumentException("Type and price cannot be null");
    }

    String zSetKey = type.equals("BUY") ? "buy_orders" : "sell_orders";

    logger.info("Fetching orders by type={}, price={}, isLessThan={}", type, price, isLessThan);

    Set<String> orders;
    if (isLessThan) {
      // Get orders with price <= given price
      orders = redisTemplate.opsForZSet().rangeByScore(zSetKey, 0, price.doubleValue());
    } else {
      // Get orders with price >= given price
      orders =
          redisTemplate
              .opsForZSet()
              .reverseRangeByScore(zSetKey, price.doubleValue(), Double.MAX_VALUE);
    }

    logger.info(
        "Fetched {} orders for type={}, price={}, isLessThan={}",
        orders.size(),
        type,
        price,
        isLessThan);

    return orders;
  }
}
