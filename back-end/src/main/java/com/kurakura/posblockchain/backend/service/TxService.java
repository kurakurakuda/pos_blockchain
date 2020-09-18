package com.kurakura.posblockchain.backend.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.kurakura.posblockchain.backend.dto.Gatya;
import com.kurakura.posblockchain.backend.dto.Purchase;
import com.kurakura.posblockchain.backend.dto.Remittance;
import com.kurakura.posblockchain.backend.dto.TxList;
import com.kurakura.posblockchain.backend.dto.UserPossession;
import com.kurakura.posblockchain.backend.entity.Block;
import com.kurakura.posblockchain.backend.entity.PendingTx;
import com.kurakura.posblockchain.backend.entity.PurchaseHistory;
import com.kurakura.posblockchain.backend.entity.Tx;
import com.kurakura.posblockchain.backend.exception.BadRequestParameterException;
import com.kurakura.posblockchain.backend.exception.BusinessLogicFailureException;
import com.kurakura.posblockchain.backend.repository.BlockRepository;
import com.kurakura.posblockchain.backend.repository.PendingTxRepository;
import com.kurakura.posblockchain.backend.repository.TxRepository;
import com.kurakura.posblockchain.infra.constants.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TxService {

    private static final int SYSTEM_USER_ID = -1;

    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private PendingTxRepository pendingTxRepository;
    @Autowired
    private BlockService blockService;
    @Autowired
    private CyptoService cyptoService;
    @Autowired
    private PendingTxService pendingTxService;
    @Autowired
    private UserService userService;
    @Autowired
    private TxRepository txRepository;

    public Gatya postGatya(Gatya gatya) {
        final Tx tx = createTx(SYSTEM_USER_ID, gatya.getRecipientId(), gatya.getAmount());

        userService.validateUserExist(tx.getRecipientId());

        String plainText = tx.createPlainText();
        
        byte[] hash = createTxHash(tx.getSenderId(), plainText);
        tx.setHash(hash);

        dealWithBlock(tx.getHash());

        if (txRepository.insertTx(tx) == 1) {
            gatya.setTimestamp(tx.getTimestamp());
            UserPossession possession = userService.calculateUserBalance(gatya.getRecipientId());
            gatya.setPossession(possession.getPossession());
            return gatya;
        } else {
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, "Failed to insert tx for gatya.");
        }
    }

    public Purchase purchaseProducts(Purchase purchase) {
        int amount = 0;
        for (PurchaseHistory ph: purchase.getProducts()) {
            amount += ph.getPrice() * ph.getCount();
        }
        final Tx tx = createTx(purchase.getSenderId(), SYSTEM_USER_ID, amount);
        tx.setProducts(purchase.getProducts());
        
        userService.validateUserExist(tx.getSenderId());
        verifyHaveEnoughMoney(tx.getSenderId(), tx.getAmount());
        
        String plainText = tx.createPlainText();

        byte[] hash = createTxHash(tx.getSenderId(), plainText);
        tx.setHash(hash);
        
        dealWithBlock(tx.getHash());
        
        if (txRepository.insertTx(tx) == 1) {
            purchase.setTimestamp(tx.getTimestamp());
        } else {
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, "Failed to insert tx for purchase.");
        }

        int c = 0;
        for (PurchaseHistory ph: tx.getProducts()) {
            ph.setHash(hash);
            txRepository.insertPurchaseHistory(ph);
            c++;
        }
        if (tx.getProducts().size() != c) {
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, "Failed to insert all purchase histories.");
        }

        UserPossession possession = userService.calculateUserBalance(purchase.getSenderId());
        purchase.setPossession(possession.getPossession());

        return purchase;
    }

    public Remittance sendMoney(Remittance remittance) {
        final Tx tx = createTx(remittance.getSenderId(), remittance.getRecipientId(), remittance.getAmount());

        userService.validateUserExist(tx.getSenderId());
        userService.validateUserExist(tx.getRecipientId());
        if (tx.getSenderId() == tx.getRecipientId()) {
            throw new BadRequestParameterException(ErrorCode.ER_400_2, String.format("Sender Id must not ne same to Recipient Id."));
        }

        verifyHaveEnoughMoney(tx.getSenderId(), tx.getAmount());

        String plainText = tx.createPlainText();

        byte[] hash = createTxHash(tx.getSenderId(), plainText);
        tx.setHash(hash);

        dealWithBlock(tx.getHash());

        if (txRepository.insertTx(tx) == 1) {
            remittance.setTimestamp(tx.getTimestamp());
            UserPossession senderPossession = userService.calculateUserBalance(remittance.getSenderId());
            remittance.setSenderPossession(senderPossession.getPossession());
            UserPossession recipientPossession = userService.calculateUserBalance(remittance.getRecipientId());
            remittance.setRecipientPossession(recipientPossession.getPossession());
            return remittance;
        } else {
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, "Failed to insert tx for remittance.");
        }
    }

    public TxList getAllTx() {
        List<Tx> allTx = txRepository.retrieveAllTx();
        TxList txs = new TxList();
        txs.setTxs(allTx);
        txs.setCount(allTx.size());
        return txs;
    }

    public TxList confirmTxTampering() {
        List<Tx> tamperingTxs = new ArrayList<>();
        List<Tx> allTx = txRepository.retrieveAllTx();
        List<Block> allBlock = blockRepository.retrieveAllBlock();
        Map<Integer, PublicKey> publicKeys = userService.retrievePublickeys();
        byte[] previousBlockHash = null;
        List<byte[]> txHashes = new ArrayList<>();
        List<Tx> txsInBlock = new ArrayList<>();

        Collections.reverse(allTx);
        int i = 0;
        for (Tx tx: allTx) {
            txHashes.add(tx.getHash());
            txsInBlock.add(tx);
            if (txHashes.size() == 10) {
                Block block = allBlock.get(i);
                byte[] actualBlockHash = blockService.generateHash(block.getIndex(),  previousBlockHash, block.getTimestamp(), txHashes, block.getNonce());
                String actualBlockHashText = new String(actualBlockHash);
                String blockHashText = new String(block.getHash());
                if (!actualBlockHashText.equals(blockHashText)) {
                    tamperingTxs.add(txsInBlock.get(0));
                    break;
                }
                txHashes.clear();
                txsInBlock.clear();
                i++;
                previousBlockHash = block.getHash();
                continue;
            }
            String originalPlanText = cyptoService.decrypto(tx.getHash(), publicKeys.get(tx.getSenderId()));
            String actualText = tx.createPlainText();
            if (!originalPlanText.equals(actualText)) {
                tamperingTxs.add(tx);
                break;
            }
        }
        // 1つ違ったら、連鎖的に一致しなくなる
        List<Tx> invalidTxs = new ArrayList<Tx>(new HashSet<>(tamperingTxs));
        TxList invalidTxList = new TxList();
        invalidTxList.setTxs(invalidTxs);
        invalidTxList.setCount(invalidTxs.size());
        return invalidTxList;
    }

    private Tx createTx(int sender, int recipient, int amount) {
        if (amount <= 0) {
            throw new BadRequestParameterException(ErrorCode.ER_400_2, "Amount must be over than 0.");
        }
        final Tx tx = new Tx();
        tx.setSenderId(sender);
        tx.setRecipientId(recipient);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return tx;
    }

    private byte[] createTxHash(int sender, String plainText) {
        String senderPrivateKey = userService.retrievePrivateKeyByUserId(sender);
        PrivateKey senderPrivateK = cyptoService.decodePrivateKey(senderPrivateKey);
        return cyptoService.encrypto(plainText, senderPrivateK);
    }

    private void dealWithBlock(byte[] hash) {
        PendingTx lastTx = pendingTxService.retrieveLastPendingTx();
        PendingTx newPendingTx = pendingTxService.insertPendingTx(lastTx, hash);

        if (newPendingTx.getSeq() != 10) {
            return;
        }

        List<PendingTx> txsForNewBlock = pendingTxRepository.retrievePendingTxsByBlockIndex(lastTx.getBlockIndex());
        List<byte[]> txHashes = new ArrayList<>();
        for (PendingTx txFNB: txsForNewBlock) {
            txHashes.add(txFNB.getTxHash());
        }
        byte[] previousBlockHash = blockService.retrieveLastBlockHash();
        Block b = blockService.generateBlock(lastTx.getBlockIndex(), previousBlockHash, txHashes);
        blockService.insertBlock(b);
    }

    private void verifyHaveEnoughMoney(int id, int amount) {
        int balance = userService.calculateUserBalance(id).getPossession();
        if (balance - amount < 0) {
            throw new BadRequestParameterException(ErrorCode.ER_400_2, String.format("User {%d} does not have enough money", id));
        }
    }
}