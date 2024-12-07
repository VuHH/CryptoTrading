INSERT INTO user_crypto (name, wallet_balance_usdt)
VALUES ('Vu', 50000.00),
       ('Bob', 2000.00),
       ('Charlie', 500.00);

INSERT INTO wallet (user_id, crypto_symbol, balance)
VALUES (1, 'BTC', 0),
       (1, 'ETH', 0);
