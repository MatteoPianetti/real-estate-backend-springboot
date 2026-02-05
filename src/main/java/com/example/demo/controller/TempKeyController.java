package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/debug")
public class TempKeyController {

    @GetMapping("/generate-key")
    public String generateKey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        
        // Stampa anche nel log per sicurezza
        System.out.println("=======================================");
        System.out.println("NUOVA CHIAVE GENERATA: " + base64Key);
        System.out.println("=======================================");
        
        return "Chiave generata. Controlla i log della console.";
    }
    
}
