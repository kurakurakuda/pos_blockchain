package com.kurakura.posblockchain.backend.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.kurakura.posblockchain.backend.dto.TxList;
import com.kurakura.posblockchain.backend.dto.UserList;
import com.kurakura.posblockchain.backend.dto.UserPossession;
import com.kurakura.posblockchain.backend.entity.User;
import com.kurakura.posblockchain.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/user")
@Validated
@Slf4j
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/initialization")
    public boolean initialize() {
        log.info("Start to insert initial users. {SystemUser, Administrator}");
        return userService.initialize();
    }

    @PostMapping
    public User createUser(@RequestParam("name") @Valid @NotBlank @Size(max = 20) String name) {
        log.info(String.format("Start to create user: %s", name));
        return userService.createUser(name);
    }

    @GetMapping(path = "/balance")
    @Cacheable(cacheNames = "userPossession", key = "'userPossession/' + #id")
    public UserPossession calculateUserBalance(@RequestParam("id") int id) {
        log.info(String.format("Start to calculate user {%d} balance", id));
        return userService.calculateUserBalance(id);
    }

    @GetMapping(path = "/txs")
    @Cacheable(cacheNames = "tx", key = "'tx/' + #id")
    public TxList getTxsByUserId(@RequestParam("id") int id) {
        log.info(String.format("Start to get all txs related to user: %d", id));
        return userService.getTxsByUserId(id);
    }

    @GetMapping
    public UserList getUserList() {
        log.info("Start to get users and administrator");
        return userService.getUserList();
    }
}