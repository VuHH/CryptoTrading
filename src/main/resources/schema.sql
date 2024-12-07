CREATE TABLE transactions
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT         NOT NULL,
    crypto_symbol    VARCHAR(255)   NOT NULL,
    transaction_type VARCHAR(255)   NOT NULL,
    amount           DECIMAL(19, 2) NOT NULL,
    price            DECIMAL(19, 2) NOT NULL,
    timestamp        TIMESTAMP      NOT NULL
);

CREATE TABLE Crypto
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    crypto_symbol VARCHAR(255),
    bid_price     DECIMAL(19, 2),
    ask_price     DECIMAL(19, 2),
    last_updated  TIMESTAMP
);

CREATE TABLE user_crypto
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255),
    wallet_balance_usdt DECIMAL(19, 2)
);

CREATE TABLE wallet
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT         NOT NULL,
    crypto_symbol VARCHAR(255)   NOT NULL,
    balance       DECIMAL(19, 2) NOT NULL
);


