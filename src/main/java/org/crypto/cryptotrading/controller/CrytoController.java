package org.crypto.cryptotrading.controller;

import org.crypto.cryptotrading.entity.Crypto;
import org.crypto.cryptotrading.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crypto")
public class CrytoController {
    @Autowired
    private CryptoService cryptoService;

    @GetMapping
    public List<Crypto> getCrypto() {
        return cryptoService.getCrypto();
    }
}
