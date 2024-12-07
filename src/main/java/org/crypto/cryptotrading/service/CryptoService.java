package org.crypto.cryptotrading.service;

import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoService {
    @Autowired
    private CryptoRepository cryptoRepository;

    public List<Crypto> getCrypto() {
        return cryptoRepository.findAll();
    }
}
