# **Crypto Trading System**

This project implements a **Crypto Trading System** using the Spring Boot framework and an in-memory H2 database. The system allows users to trade cryptocurrencies, view wallet balances, and check trading history.

---

## **Functional Scope**

1. **Trading**:
   - Users can `BUY` or `SELL` supported crypto trading pairs (`BTCUSDT`, `ETHUSDT`).
2. **View Trading History**:
   - Users can retrieve a list of their past trading transactions.
3. **Wallet Management**:
   - Users can view their cryptocurrency wallet balances.

---

## **System Assumptions**

1. Users are authenticated and authorized before accessing the APIs.
2. Each user has an initial wallet balance of **50,000 USDT** in the database.
3. Supported trading pairs:
   - Ethereum (`ETHUSDT`)
   - Bitcoin (`BTCUSDT`)

---

## **Tasks Implemented**

### **1. Price Aggregation**

- Fetches real-time pricing data from the following sources every 10 seconds:
  - **Binance**: [https://api.binance.com/api/v3/ticker/bookTicker](https://api.binance.com/api/v3/ticker/bookTicker)
  - **Huobi**: [https://api.huobi.pro/market/tickers](https://api.huobi.pro/market/tickers)
- Stores the best aggregated price (Bid for `SELL`, Ask for `BUY`) in the database.

### **2. Latest Best Price API**

- API to retrieve the latest aggregated price for supported trading pairs.

### **3. Trade API**

- Allows users to `BUY` or `SELL` cryptocurrencies based on the latest best price.
- Updates wallet balances and creates a record of the transaction.
- **Update in future: Matching Order, Partial Order, Kafka**

### **4. Wallet Balance API**

- Retrieves the current wallet balance for all cryptocurrencies for a user.

### **5. Trading History API**

- Retrieves a list of past trading transactions for a user.

---

## **Technologies Used**

- **Java 17**: Backend programming language.
- **Spring Boot**: Framework for building the application.
- **H2 Database**: In-memory database for development and testing.
- **Spring Scheduler**: For periodic price aggregation.
- **REST APIs**: For user interactions.

---

## **System Design**

### **Database Tables**

1. **`user_crypto`**:
   - Stores user details.
   - Example fields: `id`, `name`, `wallet_balance_usdt`.

2. **`wallet`**:
   - Tracks cryptocurrency balances for users.
   - Example fields: `id`, `user_id`, `crypto_symbol`, `balance`.

3. **`crypto`**:
   - Stores the latest aggregated prices for trading pairs.
   - Example fields: `id`, `crypto_symbol`, `bid_price`, `ask_price`, `last_updated`.

4. **`transactions`**:
   - Logs all trading transactions.
   - Example fields: `id`, `user_id`, `pair`, `transaction_type` (`BUY`/`SELL`), `amount`, `price`, `timestamp`.

---
### **API Endpoints**
1. **Latest Aggregated Price**:
- URL: /api/prices
- Method: GET
- Description: Fetches the latest aggregated price for supported trading pairs.
- Response:
```json
[
    {
        "cryptoSymbol": "BTC",
        "bidPrice": 100185.94,
        "askPrice": 100181.73,
        "lastUpdated": "2024-12-08T21:25:05.7640481"
    },
    {
        "cryptoSymbol": "ETH",
        "bidPrice": 3994.47,
        "askPrice": 3993.01,
        "lastUpdated": "2024-12-08T21:25:05.7640481"
    }
]
```
2. **Latest Aggregated Price By Crypto**:
- URL: /api/prices/{crypto}
- Method: GET
- Description: Fetches the latest aggregated price for crypto symol.
- Response:
```json
{
    "cryptoSymbol": "BTC",
    "bidPrice": 100203.99,
    "askPrice": 100184.94,
    "lastUpdated": "2024-12-08T21:25:10.061683"
}
```
3. **Trading**:
- URL: /api/trade
- Method: POST
- Description: send trade request
- Request:
```json
{
    "userId": 1,
    "symbol": "BTC",
    "typeTrading": "BUY",
    "amount": 0.01,
    "priceCrypto": "99787.99" 
}
```
- Response:
```json
{
    "transactionId": 3,
    "status": "SUCCESS",
    "message": "Trade executed successfully"
}
```
4. **Check Wallet**:
- URL: /api/wallet/{userId}
- Method: GET
- Description: check wallet by userId
- Response:
```json
[
    {
        "id": 1,
        "userId": 1,
        "cryptoSymbol": "BTC",
        "balance": 0.03
    },
    {
        "id": 2,
        "userId": 1,
        "cryptoSymbol": "ETH",
        "balance": 0.00
    }
]
```
5. **View Transaction**:
- URL: /api/transaction/{userId}
- Method: GET
- Description: check transactions by userId
- Response:
```json
[
    {
        "id": 1,
        "userId": 1,
        "cryptoSymbol": "BTC",
        "transactionType": "BUY",
        "amount": 0.01,
        "price": 997.88,
        "timestamp": "2024-12-08T21:25:12.644752"
    },
    {
        "id": 2,
        "userId": 1,
        "cryptoSymbol": "BTC",
        "transactionType": "BUY",
        "amount": 0.01,
        "price": 997.88,
        "timestamp": "2024-12-08T21:25:16.271641"
    },
    {
        "id": 3,
        "userId": 1,
        "cryptoSymbol": "BTC",
        "transactionType": "BUY",
        "amount": 0.01,
        "price": 997.88,
        "timestamp": "2024-12-08T21:25:17.56284"
    }
]
```
✨ Authors and contributions
Vu Ho: hohoanvu1993@gmail.com



