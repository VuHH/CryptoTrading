package org.crypto.cryptotrading.service;

import java.util.List;
import org.crypto.cryptotrading.entity.Transactions;
import org.crypto.cryptotrading.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionsRepository transactionRepository;

    public List<Transactions> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }
}
