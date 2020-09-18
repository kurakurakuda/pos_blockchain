package com.kurakura.posblockchain.backend.controller;

import javax.validation.Valid;

import com.kurakura.posblockchain.backend.dto.Gatya;
import com.kurakura.posblockchain.backend.dto.Purchase;
import com.kurakura.posblockchain.backend.dto.Remittance;
import com.kurakura.posblockchain.backend.dto.TxList;
import com.kurakura.posblockchain.backend.service.TxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/tx")
@Validated
@Slf4j
@CrossOrigin
public class TxController {

    @Autowired
    private TxService txService;

    @PostMapping(path = "/gatya")
    @Caching(evict = {
        @CacheEvict(cacheNames = "userPossession", key = "'userPossession/' + #gatya.recipientId"),
        @CacheEvict(cacheNames = "tx", key = "'tx/' + #gatya.recipientId") 
    })
    public Gatya postGatya(@RequestBody @Valid Gatya gatya) {
        log.info(String.format("Start to post gatya: %s", gatya));
        return txService.postGatya(gatya);
    }

    @PostMapping(path = "/purchase")
    @Caching(evict = {
        @CacheEvict(cacheNames = "userPossession", key = "'userPossession/' + #purchase.senderId"),
        @CacheEvict(cacheNames = "tx", key = "'tx/' + #purchase.senderId")
    })
    public Purchase purchaseProducts(@RequestBody @Valid Purchase purchase) {
        log.info(String.format("Start to purchase products: %s", purchase));
        return txService.purchaseProducts(purchase);
    }

    @PostMapping(path = "/sendmoney")
    @Caching(evict = {
        @CacheEvict(cacheNames = "userPossession", key = "'userPossession/' + #remittance.recipientId"),
        @CacheEvict(cacheNames = "userPossession", key = "'userPossession/' + #remittance.senderId"),
        @CacheEvict(cacheNames = "tx", key = "'tx/' + #remittance.recipientId"),
        @CacheEvict(cacheNames = "tx", key = "'tx/' + #remittance.senderId")
    })
    public Remittance snedMoney(@RequestBody @Valid Remittance remittance ) {
        log.info(String.format("Start to transfer money: %s", remittance));
        return txService.sendMoney(remittance);
    }

    @GetMapping(path = "/all")
    public TxList getAllTxs() {
        log.info("Start to get all tx");
        return txService.getAllTx();
    }

    @GetMapping(path = "/confirmation")
    public TxList validateTxs() {
        log.info("Start to validate all txs");
        return txService.confirmTxTampering();
    }   
}