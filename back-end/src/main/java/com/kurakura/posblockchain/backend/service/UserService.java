package com.kurakura.posblockchain.backend.service;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.kurakura.posblockchain.backend.dto.TxList;
import com.kurakura.posblockchain.backend.dto.UserList;
import com.kurakura.posblockchain.backend.dto.UserPossession;
import com.kurakura.posblockchain.backend.entity.Tx;
import com.kurakura.posblockchain.backend.entity.User;
import com.kurakura.posblockchain.backend.exception.BadRequestParameterException;
import com.kurakura.posblockchain.backend.exception.BusinessLogicFailureException;
import com.kurakura.posblockchain.backend.repository.TxRepository;
import com.kurakura.posblockchain.backend.repository.UserRepository;
import com.kurakura.posblockchain.infra.constants.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private static final int NUM_OF_NODE = 6;

    @Autowired
    private CyptoService cyptoService;
    @Autowired
    private TxRepository txRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean initialize() {
        Random rnd = new Random();

        User systemUser = new User();
        systemUser.setId(-1);
        systemUser.setName("systemUser");
        Map<String, String> keys = cyptoService.createEncodedKeys();
        systemUser.setPublicKey(keys.get("publicKey"));
        systemUser.setPrivateKey(keys.get("privateKey"));
        systemUser.setNodeAddress(rnd.nextInt(NUM_OF_NODE) + 1);
        systemUser.setAdministrator(false);
        User administrator = new User();
        administrator.setId(-2);
        administrator.setName("administrator");
        administrator.setPublicKey("");
        administrator.setPrivateKey("");
        administrator.setNodeAddress(rnd.nextInt(NUM_OF_NODE) + 1);
        administrator.setAdministrator(true);

        int result = 0;
        result = result + userRepository.insertInitialUser(systemUser);
        result = result + userRepository.insertInitialUser(administrator);
        return result == 2;
    }

    public User createUser(String name) {
        User newUser = new User();
        newUser.setName(name);
        
        Map<String, String> keys = cyptoService.createEncodedKeys();
        newUser.setPublicKey(keys.get("publicKey"));
        newUser.setPrivateKey(keys.get("privateKey"));
        
        Random rnd = new Random();
        int nodeAddress = rnd.nextInt(NUM_OF_NODE) + 1;
        newUser.setNodeAddress(nodeAddress);
        if (userRepository.insertUser(newUser) == 1) {
            return newUser;
        } else {
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, String.format("Failed to insert a User into database. name: %s", name));
        }
    }

    public void validateUserExist(int id) {
        if (id <= 0 || userRepository.isUserExist(id) == 0) {
            throw new BadRequestParameterException(ErrorCode.ER_400_2, String.format("User whose id is {%d} does not exist.", id));
        }
    }

    public String retrievePrivateKeyByUserId(int id) {
        return userRepository.retrievePrivateKeyByUserId(id);
    }

    public Map<Integer, PublicKey> retrievePublickeys() {
        final Map<Integer, PublicKey> publicKeys = new HashMap<>();
        List<User> users = userRepository.retrieveUserAndSystemUser();
        for (User user: users) {
            PublicKey pKey = cyptoService.decodPublicKey(user.getPublicKey());
            publicKeys.put(user.getId(), pKey);
        }
        return publicKeys;
    }

    public TxList getTxsByUserId(int id) {
        validateUserExist(id);
        List<Tx> txsByUser = txRepository.retrieveTxsByUser(id);
        TxList txs = new TxList();
        txs.setUserId(id);
        txs.setTxs(txsByUser);
        txs.setCount(txsByUser.size());
        int total = 0;
        for (Tx tx: txsByUser) {
            if (tx.getRecipientId() == id) {
                total += tx.getAmount();
            } else if (tx.getSenderId() == id) {
                total -= tx.getAmount();
            }
        }
        if (total < 0) {
            txs.setPossession(0);
        } else {
            txs.setPossession(total);;
        }
        return txs;
    }

    public UserPossession calculateUserBalance(int id) {
        TxList txs = getTxsByUserId(id);
        UserPossession possession = new UserPossession();
        possession.setUserId(id);
        possession.setPossession(txs.getPossession());
        return possession;
    }

    public UserList getUserList() {
        List<User> users = userRepository.retrieveUserAndAdministrator();
        UserList userList = new UserList();
        userList.setUsers(users);
        userList.setCount(users.size());
        return userList;
    }
}