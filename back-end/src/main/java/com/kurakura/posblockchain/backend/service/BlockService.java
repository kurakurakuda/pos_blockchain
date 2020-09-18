package com.kurakura.posblockchain.backend.service;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kurakura.posblockchain.backend.entity.Block;
import com.kurakura.posblockchain.backend.exception.BusinessLogicFailureException;
import com.kurakura.posblockchain.backend.repository.BlockRepository;
import com.kurakura.posblockchain.infra.constants.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BlockService {

    private static final String PROOF_OF_KEY = "96";

    @Autowired
    private BlockRepository blockRepository;

    public Block generateBlock(int index, byte[] previousBlockHash, List<byte[]> txs) {
        final Block generatedBlock = new Block();
        generatedBlock.setIndex(index);
        generatedBlock.setPreviousBlockHash(previousBlockHash);
        generatedBlock.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        generatedBlock.setTxs(txs);

        int nonce = 0;

        byte[] hash = generateHash(generatedBlock.getIndex(), generatedBlock.getPreviousBlockHash(),
                generatedBlock.getTimestamp(), txs, nonce);
        // Proof of Work
        while (!hash.toString().substring(3, 5).equals(PROOF_OF_KEY)) {
            nonce++;
            hash = generateHash(generatedBlock.getIndex(), generatedBlock.getPreviousBlockHash(),
                    generatedBlock.getTimestamp(), txs, nonce);
        }

        generatedBlock.setHash(hash);
        generatedBlock.setNonce(nonce);
        return generatedBlock;
    }

    public byte[] retrieveLastBlockHash() {
        Block previousBlock = blockRepository.retrieveLastBlock();
        if (!Objects.equals(previousBlock, null)) {
            return previousBlock.getHash();
        }
        return null;
    }

    public Block insertBlock(Block block) {
        if (blockRepository.insertBlock(block) == 1) {
            return block;
        } else {
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, "Failed to insert block.");
        }
    }

    public byte[] generateHash(int index, byte[] previousBlockHash, LocalDateTime timestamp, List<byte[]> txs, int nonce) {
        List<String> plainTextList = new ArrayList<>();
        plainTextList.add(String.valueOf(index));
        if (Objects.equals(previousBlockHash, null)) {
            plainTextList.add(null);
        } else {
            plainTextList.add(previousBlockHash.toString());
        }
        plainTextList.add(timestamp.toString());
        plainTextList.add(String.valueOf(nonce));
        byte[] plainHashes = plainTextList.toString().getBytes();
        for (byte[] txHash: txs) {
            byte[] allByteArray = new byte[plainHashes.length + txHash.length];

            ByteBuffer buff = ByteBuffer.wrap(allByteArray);
            buff.put(plainHashes);
            buff.put(txHash);
            
            plainHashes = buff.array();
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(plainHashes);
        } catch (NoSuchAlgorithmException e) {
            log.error(String.format("Failed to generate hash to create block.  %s: %s", e.getClass(), e.getMessage()));
            throw new BusinessLogicFailureException(ErrorCode.ER_500_1, "Failed to generate hash to create block.");
        }
    }
}